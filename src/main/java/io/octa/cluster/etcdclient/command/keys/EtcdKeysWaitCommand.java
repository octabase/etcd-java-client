package io.octa.cluster.etcdclient.command.keys;

import java.net.URI;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;

public class EtcdKeysWaitCommand extends EtcdKeysCommand {
    private final static RequestConfig NO_TIMEOUT;

    private String key = null;
    private long waitIndex = 0;
    private boolean recursive = false;

    static {
        NO_TIMEOUT = RequestConfig.custom().setConnectionRequestTimeout(0).build();
    }

    public String getKey() {
        return key;
    }

    public long getWaitIndex() {
        return waitIndex;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public EtcdKeysWaitCommand withKey(String key) {
        this.key = key;

        return this;
    }

    public EtcdKeysWaitCommand withWaitIndex(long waitIndex) {
        this.waitIndex = waitIndex;

        return this;
    }

    public EtcdKeysWaitCommand withRecursive(boolean recursive) {
        this.recursive = recursive;

        return this;
    }

    @Override
    protected void buildUriPathElements(Consumer<String> emitter) {
        emitter.accept(API_PATH);
        emitter.accept(getPrefix());
        emitter.accept(getKey());
    }

    @Override
    protected void buildQueryParams(BiConsumer<String, String> emitter) {
        emitter.accept("wait", "true");

        if (waitIndex > 0) {
            emitter.accept("waitIndex", Long.toString(waitIndex));
        }

        if (recursive) {
            emitter.accept("recursive", "true");
        }
    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        return RequestBuilder.get(uri).setConfig(NO_TIMEOUT);
    }
}
