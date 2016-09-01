package io.octa.cluster.etcdclient.command.keys;

import java.net.URI;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.http.client.methods.RequestBuilder;

public class EtcdKeysGetCommand extends EtcdKeysCommand {
    private String key = null;
    private boolean recursive = false;
    private boolean sorted = false;
    private boolean quorum = false;

    public String getKey() {
        return key;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public boolean isSorted() {
        return sorted;
    }

    public boolean isQuorum() {
        return quorum;
    }

    public EtcdKeysGetCommand withKey(String key) {
        this.key = key;

        return this;
    }

    public EtcdKeysGetCommand withRecursive(boolean recursive) {
        this.recursive = recursive;

        return this;
    }

    public EtcdKeysGetCommand withSorted(boolean sorted) {
        this.sorted = sorted;

        return this;
    }

    public EtcdKeysGetCommand withQuorum(boolean quorum) {
        this.quorum = quorum;

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
        if (recursive) {
            emitter.accept("recursive", "true");
        }

        if (sorted) {
            emitter.accept("sorted", "true");
        }

        if (quorum) {
            emitter.accept("quorum", "true");
        }
    }

    @Override
    protected RequestBuilder createRequestBuilder(URI uri) {
        return RequestBuilder.get(uri);
    }
}
