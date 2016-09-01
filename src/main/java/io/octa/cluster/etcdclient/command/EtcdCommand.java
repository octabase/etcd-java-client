package io.octa.cluster.etcdclient.command;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.octa.cluster.etcdclient.exceptions.MalformedEtcdResponseException;
import io.octa.cluster.etcdclient.model.EtcdId;

public abstract class EtcdCommand<R> {
    protected final static ObjectMapper jsonMapper = new ObjectMapper();

    public abstract R readResponse(HttpResponse httpResponse);

    public void handleError(Throwable cause) {
        //
    }

    protected abstract void buildUriPathElements(Consumer<String> emitter);
    protected abstract RequestBuilder createRequestBuilder(URI uri);

    protected void buildQueryParams(BiConsumer<String, String> emitter) {
        //
    }

    protected void buildPostParams(BiConsumer<String, String> emitter) {
        //
    }

    protected String buildReqBody() {
        return null;
    }

    public EtcdId readClusterId(HttpResponse httpResponse) {
        Header[] clusterIdHdrs = httpResponse.getHeaders("X-Etcd-Cluster-Id");

        if ((clusterIdHdrs == null) || (clusterIdHdrs.length == 0)) {
            throw new MalformedEtcdResponseException("`X-Etcd-Cluster-Id` header not found.");

        } else if (clusterIdHdrs.length != 1) {
            throw new MalformedEtcdResponseException("multiple `X-Etcd-Cluster-Id` header was found.");
        }

        try {
            return new EtcdId(clusterIdHdrs[0].getValue());

        } catch (NumberFormatException e) {
            throw new MalformedEtcdResponseException("unable to parse `X-Etcd-Cluster-Id` header('" + clusterIdHdrs[0].getValue() + "'): " + e.getMessage(), e);
        }
    }

    public HttpUriRequest createRequest(String etcdEndpoint) {
        try {
            URIBuilder uriBuilder = new URIBuilder(etcdEndpoint);

            ArrayList<String> pathEls = new ArrayList<>();
            ArrayList<NameValuePair> queryParams = new ArrayList<>();
            ArrayList<NameValuePair> postParams = new ArrayList<>();

            String reqBody = buildReqBody();

            buildUriPathElements(pathEls::add);
            buildQueryParams((k, v) -> queryParams.add(new BasicNameValuePair(k, v)));

            if (reqBody != null) {
                buildPostParams((k, v) -> postParams.add(new BasicNameValuePair(k, v)));
            }

            StringBuilder pathBuilder = new StringBuilder();

            for (String pathEl: pathEls) {
                if (pathEl == null) {
                    continue;
                }

                pathEl = pathEl.trim();
                if (pathEl.isEmpty()) {
                    continue;
                }

                int start = 0;
                while ((start < pathEl.length()) && ((pathEl.charAt(start) == '/') || (pathEl.charAt(start) == '.'))) {
                    start++;
                }

                int end = pathEl.length();
                while ((end > 0) && (pathEl.charAt(end - 1) == '/')) {
                    end--;
                }

                if ((end - start) < 1) {
                    continue;
                }

                pathBuilder.append('/');
                pathBuilder.append(pathEl, start, end);
            }

            if (pathBuilder.length() > 0) {
                uriBuilder.setPath(pathBuilder.toString());
            }

            if (!queryParams.isEmpty()) {
                uriBuilder.addParameters(queryParams);
            }

            RequestBuilder reqBuilder = createRequestBuilder(uriBuilder.build());

            if (reqBody == null) {
                if (!postParams.isEmpty()) {
                    reqBuilder.setEntity(new UrlEncodedFormEntity(postParams));
                }

            } else {
                reqBuilder.setEntity(new StringEntity(reqBody, StandardCharsets.UTF_8));
            }

            return reqBuilder.build();

        } catch (URISyntaxException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
