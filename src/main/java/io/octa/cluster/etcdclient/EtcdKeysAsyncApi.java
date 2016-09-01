package io.octa.cluster.etcdclient;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.octa.cluster.etcdclient.command.keys.EtcdKeysCommand;
import io.octa.cluster.etcdclient.command.keys.EtcdKeysCreateInOrderCommand;
import io.octa.cluster.etcdclient.command.keys.EtcdKeysDeleteCommand;
import io.octa.cluster.etcdclient.command.keys.EtcdKeysGetCommand;
import io.octa.cluster.etcdclient.command.keys.EtcdKeysSetCommand;
import io.octa.cluster.etcdclient.command.keys.EtcdKeysWaitCommand;
import io.octa.cluster.etcdclient.model.EtcdKeysResponse;

public class EtcdKeysAsyncApi {
    private EtcdClient client;
    private EtcdKeysApi syncApi;

    private String keyPrefix;

    EtcdKeysAsyncApi(EtcdClient client, String keyPrefix) {
        this.client = client;
        this.keyPrefix = keyPrefix;
    }

    public EtcdKeysApi sync() {
        if (syncApi == null) {
            syncApi = new EtcdKeysApi(this);
        }

        return syncApi;
    }

    private CompletableFuture<EtcdKeysResponse> execute(EtcdKeysCommand command) {
        command.setPrefix(keyPrefix);

        return client.execute(command);
    }

    public CompletableFuture<EtcdKeysResponse> set(String key, String value) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value));
    }

    public CompletableFuture<EtcdKeysResponse> set(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withTtl(ttl, ttlTimeUnit));
    }

    public CompletableFuture<EtcdKeysResponse> compareAndSwap(String key, String expectedValue, String value) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevValue(expectedValue));
    }

    public CompletableFuture<EtcdKeysResponse> compareAndSwap(String key, String expectedValue, String value, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevValue(expectedValue)
                .withTtl(ttl, ttlTimeUnit));
    }

    public CompletableFuture<EtcdKeysResponse> compareAndSwap(String key, long expectedModifiedIndex, String value) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevIndex(expectedModifiedIndex));
    }

    public CompletableFuture<EtcdKeysResponse> compareAndSwap(String key, long expectedModifiedIndex, String value, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevIndex(expectedModifiedIndex)
                .withTtl(ttl, ttlTimeUnit));
    }

    public CompletableFuture<EtcdKeysResponse> setIfExists(String key, String value) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevExists(true));
    }

    public CompletableFuture<EtcdKeysResponse> setIfExists(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevExists(true)
                .withTtl(ttl, ttlTimeUnit));
    }

    public CompletableFuture<EtcdKeysResponse> setIfNotExists(String key, String value) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevExists(false));
    }

    public CompletableFuture<EtcdKeysResponse> setIfNotExists(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withValue(value)
                .withPrevExists(false)
                .withTtl(ttl, ttlTimeUnit));
    }

    public CompletableFuture<EtcdKeysResponse> delete(String key) {
        return execute(new EtcdKeysDeleteCommand()
                .withKey(key));
    }

    public CompletableFuture<EtcdKeysResponse> deleteDirectory(String key) {
        return execute(new EtcdKeysDeleteCommand()
                .withKey(key)
                .withDir(true));
    }

    public CompletableFuture<EtcdKeysResponse> compareAndDelete(String key, String expectedValue) {
        return execute(new EtcdKeysDeleteCommand()
                .withKey(key)
                .withPrevValue(expectedValue));
    }

    public CompletableFuture<EtcdKeysResponse> compareAndDelete(String key, long expectedModifiedIndex) {
        return execute(new EtcdKeysDeleteCommand()
                .withKey(key)
                .withPrevIndex(expectedModifiedIndex));
    }

    public CompletableFuture<EtcdKeysResponse> refresh(String key, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withTtl(ttl, ttlTimeUnit)
                .withRefresh(true));
    }

    public CompletableFuture<EtcdKeysResponse> createDirectory(String key) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withDir(true));
    }

    public CompletableFuture<EtcdKeysResponse> createDirectory(String key, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withTtl(ttl, ttlTimeUnit)
                .withDir(true));
    }

    public CompletableFuture<EtcdKeysResponse> refreshIfExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withTtl(ttl, ttlTimeUnit)
                .withRefresh(true)
                .withPrevExists(true));
    }

    public CompletableFuture<EtcdKeysResponse> refreshIfNotExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withTtl(ttl, ttlTimeUnit)
                .withRefresh(true)
                .withPrevExists(false));
    }

    public CompletableFuture<EtcdKeysResponse> refreshDirectory(String key, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withTtl(ttl, ttlTimeUnit)
                .withDir(true)
                .withRefresh(true));
    }

    public CompletableFuture<EtcdKeysResponse> refreshDirectoryIfExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withTtl(ttl, ttlTimeUnit)
                .withRefresh(true)
                .withDir(true)
                .withPrevExists(true));
    }

    public CompletableFuture<EtcdKeysResponse> refreshDirectoryIfNotExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysSetCommand()
                .withKey(key)
                .withTtl(ttl, ttlTimeUnit)
                .withRefresh(true)
                .withDir(true)
                .withPrevExists(false));
    }

    public CompletableFuture<EtcdKeysResponse> createByInOrderKey(String parentKey, String value) {
        return execute(new EtcdKeysCreateInOrderCommand()
                .withDir(parentKey)
                .withValue(value));
    }

    public CompletableFuture<EtcdKeysResponse> createByInOrderKey(String parentKey, String value, long ttl, TimeUnit ttlTimeUnit) {
        return execute(new EtcdKeysCreateInOrderCommand()
                .withDir(parentKey)
                .withTtl(ttl, ttlTimeUnit)
                .withValue(value));
    }

    public CompletableFuture<EtcdKeysResponse> get(String key) {
        return execute(new EtcdKeysGetCommand()
                .withKey(key));
    }

    public CompletableFuture<EtcdKeysResponse> get(String key, boolean recursive) {
        return execute(new EtcdKeysGetCommand()
                .withKey(key)
                .withRecursive(recursive));
    }

    public CompletableFuture<EtcdKeysResponse> getQuorum(String key) {
        return execute(new EtcdKeysGetCommand()
                .withKey(key)
                .withQuorum(true));
    }

    public CompletableFuture<EtcdKeysResponse> getQuorum(String key, boolean recursive) {
        return execute(new EtcdKeysGetCommand()
                .withKey(key)
                .withQuorum(true)
                .withRecursive(recursive));
    }

    public CompletableFuture<EtcdKeysResponse> watch(String key) {
        return execute(new EtcdKeysWaitCommand()
                .withKey(key));
    }

    public CompletableFuture<EtcdKeysResponse> watch(String key, boolean recursive) {
        return execute(new EtcdKeysWaitCommand()
                .withKey(key)
                .withRecursive(recursive));
    }

    public CompletableFuture<EtcdKeysResponse> watch(String key, long waitIndex) {
        return execute(new EtcdKeysWaitCommand()
                .withKey(key)
                .withWaitIndex(waitIndex));
    }

    public CompletableFuture<EtcdKeysResponse> watch(String key, boolean recursive, long waitIndex) {
        return execute(new EtcdKeysWaitCommand()
                .withKey(key)
                .withRecursive(recursive)
                .withWaitIndex(waitIndex));
    }
}
