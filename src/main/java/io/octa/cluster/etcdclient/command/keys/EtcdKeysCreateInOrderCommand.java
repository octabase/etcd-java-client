package io.octa.cluster.etcdclient.command.keys;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.http.client.methods.RequestBuilder;

public class EtcdKeysCreateInOrderCommand extends EtcdKeysCommand {
    private String dir = null;
    private String value = null;
    private long ttl = 0;
    private TimeUnit ttlUnit = null;

    public String getDir() {
        return dir;
    }

    public String getValue() {
        return value;
    }

    public long getTtl() {
        return ttl;
    }

    public TimeUnit getTtlUnit() {
        return ttlUnit;
    }

    public EtcdKeysCreateInOrderCommand withDir(String dir) {
        this.dir = dir;

        return this;
    }

    public EtcdKeysCreateInOrderCommand withValue(String value) {
        this.value = value;

        return this;
    }

    public EtcdKeysCreateInOrderCommand withTtl(long ttl, TimeUnit unit) {
        if ((ttl <= 0) || (unit == null)) {
            this.ttl = 0;
            ttlUnit = null;

        } else {
            this.ttl = ttl;
            ttlUnit = unit;
        }

        return this;
    }

    @Override
    protected void buildUriPathElements(Consumer<String> emitter) {
        emitter.accept(API_PATH);
        emitter.accept(getPrefix());
        emitter.accept(getDir());
    }

    @Override
    protected void buildQueryParams(BiConsumer<String, String> emitter) {
        if ((ttl > 0) && (ttlUnit != null)) {
            emitter.accept("ttl", Long.toString(ttlUnit.toSeconds(ttl)));
        }
    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        return RequestBuilder.post(uri);
    }

    @Override
    protected void buildPostParams(BiConsumer<String, String> emitter) {
        emitter.accept("value", value);
    }
}
