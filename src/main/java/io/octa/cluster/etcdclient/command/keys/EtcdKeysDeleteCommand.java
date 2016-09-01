package io.octa.cluster.etcdclient.command.keys;

import java.net.URI;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.http.client.methods.RequestBuilder;

public class EtcdKeysDeleteCommand extends EtcdKeysCommand {
    private String key = null;
    private String prevValue = null;
    private long prevIndex = 0;
    private boolean dir = false;
    private boolean recursive = false;

    public String getKey() {
        return key;
    }

    public String getPrevValue() {
        return prevValue;
    }

    public long getPrevIndex() {
        return prevIndex;
    }

    public boolean isDir() {
        return dir;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public EtcdKeysDeleteCommand withKey(String key) {
        this.key = key;

        return this;
    }

    public EtcdKeysDeleteCommand withPrevValue(String prevValue) {
        this.prevValue = prevValue;

        return this;
    }

    public EtcdKeysDeleteCommand withPrevIndex(long prevIndex) {
        this.prevIndex = prevIndex;

        return this;
    }

    public EtcdKeysDeleteCommand withDir(boolean dir) {
        this.dir = dir;

        return this;
    }

    public EtcdKeysDeleteCommand withRecursive(boolean recursive) {
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
        if (prevIndex > 0) {
            emitter.accept("prevIndex", Long.toString(prevIndex));
        }

        if (dir) {
            emitter.accept("dir", "true");
        }

        if (recursive) {
            emitter.accept("recursive", "true");
        }
    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        return RequestBuilder.delete(uri);
    }

    @Override
    protected void buildPostParams(BiConsumer<String, String> emitter) {
        if (prevValue != null) {
            emitter.accept("prevValue", prevValue);
        }
    }
}
