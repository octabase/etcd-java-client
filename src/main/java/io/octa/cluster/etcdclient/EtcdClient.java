package io.octa.cluster.etcdclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.octa.cluster.etcdclient.command.EtcdCommand;
import io.octa.cluster.etcdclient.exceptions.EtcdRemoteException;

public class EtcdClient implements AutoCloseable {
    private final Conf conf;

    final CloseableHttpAsyncClient httpClient;

    public static void main(String... args) throws Exception {
        EtcdClient etcd = EtcdClient.builder()
                .withPropertiesClasspathFileIfExists("hoba.properties")
                .withPropertiesFileIfExists("/etc/default/hoba.properties")
                .withSystemProperties()
                .build();

       // System.out.println(etcd.keysApi().get("hoba").getNode().getValue());

    //    System.out.println(Thread.currentThread() + "| " + 1);
//
       // CompletableFuture<EtcdEvent> f = etcd.asyncKeysApi().get("/", true);
/*
        f.whenComplete((response, error) -> {
            System.out.println(Thread.currentThread() + "| " + 3);
            System.out.println("error: " + error);
            System.out.println(response);
        });
*/
       // f.cancel(true);

      //  System.out.println(Thread.currentThread() + "| " + 2);


        System.out.println(etcd.asyncMembersApi().getMembers().get());

        System.out.println(etcd.asyncMembersApi().getLeader().get());

       // System.out.println(etcd.keysApi().get("test").asValue());

        System.out.println(etcd.asyncMembersApi().addMember("http://etcd4:2379").get().getPayload());

        etcd.close();
    }

    String getEtcdEndpoint() {
        return conf.contactPoints.iterator().next();
    }

    public EtcdKeysApi keysApi() {
        return asyncKeysApi(null).sync();
    }

    public EtcdKeysApi keysApi(String keyPrefix) {
        return asyncKeysApi(keyPrefix).sync();
    }

    public EtcdKeysAsyncApi asyncKeysApi() {
        return asyncKeysApi(null);
    }

    private EtcdKeysAsyncApi defaultKeysApi = null;
    private EtcdMembersAsyncApi defaultMembersApi = null;

    public EtcdKeysAsyncApi asyncKeysApi(String keyPrefix) {
        if (keyPrefix == null) {
            if (defaultKeysApi == null) {
                defaultKeysApi = new EtcdKeysAsyncApi(this, null);
            }

            return defaultKeysApi;
        }

        return new EtcdKeysAsyncApi(this, keyPrefix);
    }

    public EtcdMembersAsyncApi asyncMembersApi() {
        if (defaultMembersApi == null) {
            defaultMembersApi = new EtcdMembersAsyncApi(this);
        }

        return defaultMembersApi;
    }

    private EtcdClient(Conf conf) {
        this.conf = conf;

        RequestConfig.Builder reqConfBuilder = RequestConfig.custom();

        reqConfBuilder.setConnectionRequestTimeout(10);
        reqConfBuilder.setConnectTimeout(10);
        reqConfBuilder.setSocketTimeout(10);
        reqConfBuilder.setContentCompressionEnabled(false);
        reqConfBuilder.setExpectContinueEnabled(false);
        reqConfBuilder.setMaxRedirects(2);

        HttpAsyncClientBuilder httpClientBuilder = HttpAsyncClients.custom();

        if (conf.proxy) {
            httpClientBuilder.setProxy(new HttpHost(conf.proxyHost, conf.proxyPort));
        }

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        if (conf.auth) {
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(conf.username, conf.password));
        }

        if (conf.proxyAuth) {
            credentialsProvider.setCredentials(new AuthScope(conf.proxyHost, conf.proxyPort, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME), new UsernamePasswordCredentials(conf.proxyUsername, conf.proxyPassword));
        }

        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        httpClientBuilder.disableCookieManagement();
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        httpClientBuilder.setUserAgent("koala");
        httpClientBuilder.setDefaultRequestConfig(reqConfBuilder.build());

        httpClient = httpClientBuilder.build();

        httpClient.start();
    }

    @Override
    public void close() throws Exception {
        httpClient.close();
    }

    private final static ObjectMapper jsonMapper = new ObjectMapper();

    private void handleRemoteException(HttpResponse response) {
        if (response == null) {
            return;
        }

        int status = response.getStatusLine().getStatusCode();

        if ((status >= 200) && (status <= 299)) {
            return;
        }

        Header contentType = response.getFirstHeader("Content-Type");

        if (contentType != null) {
            if (contentType.getValue().contains("/json")) {
                try {
                    JsonNode node = jsonMapper.readTree(response.getEntity().getContent());

                    Integer errorCode = -1;
                    String message = "";
                    String cause = "";

                    JsonNode errorCodeNode = node.get("errorCode");
                    if ((errorCodeNode != null) && !errorCodeNode.isNull() && errorCodeNode.isNumber()) {
                        errorCode = errorCodeNode.asInt();
                    }

                    JsonNode messageNode = node.get("message");
                    if ((messageNode != null) && !messageNode.isNull() && messageNode.isTextual()) {
                        message = messageNode.asText();
                    }

                    JsonNode causeNode = node.get("cause");
                    if ((causeNode != null) && !causeNode.isNull() && causeNode.isTextual()) {
                        cause = causeNode.asText();
                    }

                    EtcdRemoteException ex = new EtcdRemoteException((errorCode == -1 ? status : errorCode) + ": " + message + "(" + cause + ")");
                    ex.setEtcdErrorCode(errorCode);
                    ex.setEtcdErrorMessage(message);
                    ex.setEtcdErrorCause(cause);

                    throw ex;

                } catch (EtcdRemoteException e) {
                    throw e;

                } catch (Exception e) {
                    throw new EtcdRemoteException("unable to read etcd response: " + e.getMessage(), e);
                }
            }
        }

        try {
            throw new EtcdRemoteException("HTTP " + status + ": " + EntityUtils.toString(response.getEntity()));

        } catch (ParseException | IOException e) {
            throw new EtcdRemoteException("HTTP " + status + ": " + response.getStatusLine().getReasonPhrase());
        }
    }

    private <T> void doExecute(AtomicReference<Future<HttpResponse>> httpFutureRef, EtcdCommand<T> command, String endpoint, Consumer<Throwable> errorHandler, Consumer<T> resultHandler) {
        httpFutureRef.set(httpClient.execute(command.createRequest(endpoint), new FutureCallback<HttpResponse>() {
            @Override
            public void failed(Exception e) {
                try {
                    command.handleError(e);

                    errorHandler.accept(e);

                } catch (Throwable ht) {
                    errorHandler.accept(ht);
                }
            }

            @Override
            public void completed(HttpResponse result) {
                try {
                    handleRemoteException(result);

                    resultHandler.accept(command.readResponse(result));

                } catch (Throwable t) {
                    try {
                        command.handleError(t);

                        errorHandler.accept(t);

                    } catch (Throwable ht) {
                        errorHandler.accept(ht);
                    }
                }
            }

            @Override
            public void cancelled() {
                //
            }
        }));
    }

    public <T> CompletableFuture<T> execute(EtcdCommand<T> command) {
        AtomicReference<Future<HttpResponse>> httpFutureRef = new AtomicReference<>();

        CompletableFuture<T> completableFuture = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                Future<HttpResponse> httpFuture = httpFutureRef.getAndSet(null);
                if (httpFuture != null) {
                    return httpFuture.cancel(mayInterruptIfRunning);
                }

                return false;
            }
        };

        doExecute(httpFutureRef, command, getEtcdEndpoint(), completableFuture::completeExceptionally, completableFuture::complete);

        return completableFuture;
    }

    public static Builder builder() {
        return new Builder();
    }

    private static final class Conf {
        private boolean auth;
        private String username;
        private String password;
        private boolean proxy;
        private boolean proxyAuth;
        private String proxyHost;
        private int proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private Set<String> contactPoints = new HashSet<>();
        private Map<String, String> addressTranslations = new HashMap<>();
    }

    public static final class Builder {
        private final Conf conf;

        private Builder() {
            conf = new Conf();
        }

        public Builder withContactPoint(String uri) {
            return withContactPoints(Objects.requireNonNull(uri, "uri can not be null."));
        }

        public Builder withContactPoints(String... uri) {
            return withContactPoints(Arrays.asList(Objects.requireNonNull(uri, "uri can not be null.")));
        }

        public Builder withContactPoints(Iterable<String> uris) {
            for (String uri: Objects.requireNonNull(uris, "uris can not be null.")) {
                conf.contactPoints.add(Objects.requireNonNull(uri, "uri can not be null."));
            }

            return this;
        }

        public Builder withAuthentication(String username, String password) {
            conf.auth = true;
            conf.username = Objects.requireNonNull(username, "username can not be null.");
            conf.password = Objects.requireNonNull(password, "password can not be null.");

            return this;
        }

        public Builder withProxy(String proxyHost, int proxyPort) {
            conf.proxy = true;
            conf.proxyHost = Objects.requireNonNull(proxyHost, "proxyHost can not be null.");
            conf.proxyPort = proxyPort;

            return this;
        }

        public Builder withProxy(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
            conf.proxyAuth = true;
            conf.proxyUsername = proxyUsername;
            conf.proxyPassword = proxyPassword;

            return withProxy(proxyHost, proxyPort);
        }

        public Builder withAddressTranslation(String fromAddress, String toAddress) {
            conf.addressTranslations.put(Objects.requireNonNull(fromAddress, "fromAddress can not be null."), Objects.requireNonNull(toAddress, "toAddress can not be null."));

            return this;
        }

        public Builder withPropertiesFileIfExists(String... filenames) {
            for (String filename: Objects.requireNonNull(filenames, "filenames can not be null.")) {
                File file = new File(Objects.requireNonNull(filename, "filename can not be null."));
                if (file.exists() && file.isFile() && file.canRead()) {
                    try (InputStream inStream = new FileInputStream(file)) {
                        Properties props = new Properties();

                        props.load(inStream);

                        return withProperties(props);

                    } catch (IOException e) {
                        throw new RuntimeException("Unable to read properties file: " + filename, e);
                    }
                }
            }

            return this;
        }

        public Builder withPropertiesClasspathFileIfExists(String filename) {
            try (InputStream inStream = getClass().getClassLoader()
                    .getResourceAsStream(Objects.requireNonNull(filename, "filename can not be null."))) {
                if (inStream != null) {
                    Properties props = new Properties();

                    props.load(inStream);

                    return withProperties(props);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to read properties file: " + filename, e);
            }

            return this;
        }

        public Builder withSystemProperties() {
            return withProperties(System.getProperties());
        }

        public Builder withProperties(Properties properties) {
            return withPropertiesMap(Objects.requireNonNull(properties, "properties can not be null."));
        }

        public Builder withPropertiesMap(Map<?, ?> propertiesMap) {
            Objects.requireNonNull(propertiesMap, "propertiesMap can not be null.");

            Object contactPoints = propertiesMap.get("etcdContactPoints");
            Object username = propertiesMap.get("etcdUsername");
            Object password = propertiesMap.get("etcdPassword");
            Object proxyHost = propertiesMap.get("etcdProxyHost");
            Object proxyPort = propertiesMap.get("etcdProxyPort");
            Object proxyUsername = propertiesMap.get("etcdProxyUsername");
            Object proxyPassword = propertiesMap.get("etcdProxyPassword");
            Object addressTranslations = propertiesMap.get("etcdAddressTranslations");

            if ((proxyHost != null) && (proxyPort != null)) {
                if ((proxyUsername != null) && (proxyPassword != null)) {
                    withProxy(proxyHost.toString(), Integer.parseInt(proxyPort.toString()), proxyUsername.toString(), proxyPassword.toString());

                } else {
                    withProxy(proxyHost.toString(), Integer.parseInt(proxyPort.toString()));
                }
            }

            if ((username != null) && (password != null)) {
                withAuthentication(username.toString(), password.toString());
            }

            if (contactPoints != null) {
                withContactPoints(contactPoints.toString().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
            }

            if (addressTranslations != null) {
                for (String addressTranslation: addressTranslations.toString().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")) {
                    String[] definition = addressTranslation.split("\\=(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (definition.length != 2) {
                        throw new IllegalArgumentException("Invalid address translate definition syntax: " + addressTranslation + "[" + addressTranslations + "]");
                    }

                    withAddressTranslation(definition[0], definition[1]);
                }
            }

            return this;
        }

        public EtcdClient build() {
            return new EtcdClient(conf);
        }
    }
}
