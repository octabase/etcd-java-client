package io.octa.cluster.etcdclient.command.keys;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.http.client.methods.RequestBuilder;

public class EtcdKeysSetCommand extends EtcdKeysCommand {
    private String key = null;
    private String value = null;
    private String prevValue = null;
    private long prevIndex = 0;
    private Boolean prevExists = null;
    private long ttl = 0;
    private TimeUnit ttlUnit = null;
    private boolean refresh = false;
    private boolean dir = false;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getPrevValue() {
        return prevValue;
    }

    public long getPrevIndex() {
        return prevIndex;
    }

    public Boolean getPrevExists() {
        return prevExists;
    }

    public long getTtl() {
        return ttl;
    }

    public TimeUnit getTtlUnit() {
        return ttlUnit;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public boolean isDir() {
        return dir;
    }

    public EtcdKeysSetCommand withKey(String key) {
        this.key = key;

        return this;
    }

    public EtcdKeysSetCommand withValue(String value) {
        this.value = value;

        return this;
    }

    public EtcdKeysSetCommand withPrevValue(String prevValue) {
        this.prevValue = prevValue;

        return this;
    }

    public EtcdKeysSetCommand withPrevIndex(long prevIndex) {
        this.prevIndex = prevIndex;

        return this;
    }

    public EtcdKeysSetCommand withPrevExists(Boolean prevExists) {
        this.prevExists = prevExists;

        return this;
    }

    public EtcdKeysSetCommand withTtl(long ttl, TimeUnit unit) {
        if ((ttl <= 0) || (unit == null)) {
            this.ttl = 0;
            ttlUnit = null;

        } else {
            this.ttl = ttl;
            ttlUnit = unit;
        }

        return this;
    }

    public EtcdKeysSetCommand withRefresh(boolean refresh) {
        this.refresh = refresh;

        return this;
    }

    public EtcdKeysSetCommand withDir(boolean dir) {
        this.dir = dir;

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
        if (prevExists != null) {
            emitter.accept("prevExists", prevExists ? "true" : "false");
        }

        if (prevIndex > 0) {
            emitter.accept("prevIndex", Long.toString(prevIndex));
        }

        if ((ttl > 0) && (ttlUnit != null)) {
            emitter.accept("ttl", Long.toString(ttlUnit.toSeconds(ttl)));
        }

        if (refresh) {
            emitter.accept("refresh", "true");
        }

        if (dir) {
            emitter.accept("dir", "true");
        }
    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        return RequestBuilder.put(uri);
    }

    @Override
    protected void buildPostParams(BiConsumer<String, String> emitter) {
        if (value != null) {
            emitter.accept("value", value);
        }

        if (prevValue != null) {
            emitter.accept("prevValue", prevValue);
        }
    }
}
