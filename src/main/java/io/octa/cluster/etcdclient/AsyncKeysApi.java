package io.octa.cluster.etcdclient;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AsyncKeysApi {
    private final EtcdClient client;

    AsyncKeysApi(EtcdClient client) {
        this.client = client;
    }

    public Future<AsyncKeysApi> set(String key, String value) {
        return null;
    }

    public Future<AsyncKeysApi> set(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> compareAndSwap(String key, String expectedValue, String value) {
        return null;
    }

    public Future<AsyncKeysApi> compareAndSwap(String key, String expectedValue, String value, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> compareAndSwap(String key, long expectedModifiedIndex, String value) {
        return null;
    }

    public Future<AsyncKeysApi> compareAndSwap(String key, long expectedModifiedIndex, String value, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> setIfExists(String key, String value) {
        return null;
    }

    public Future<AsyncKeysApi> setIfExists(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> setIfNotExists(String key, String value) {
        return null;
    }

    public Future<AsyncKeysApi> setIfNotExists(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> delete(String key) {
        return null;
    }

    public Future<AsyncKeysApi> compareAndDelete(String key, String expectedValue) {
        return null;
    }

    public Future<AsyncKeysApi> compareAndDelete(String key, long expectedModifiedIndex) {
        return null;
    }

    public Future<AsyncKeysApi> refresh(String key, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> refreshIfExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> refreshIfNotExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> refreshDirectory(String key, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> refreshDirectoryIfExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> refreshDirectoryIfNotExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> createByInOrderKey(String parentKey, String value) {
        return null;
    }

    public Future<AsyncKeysApi> createByInOrderKey(String parentKey, String value, long ttl, TimeUnit ttlTimeUnit) {
        return null;
    }

    public Future<AsyncKeysApi> get(String key) {
        return null;
    }

    public Future<AsyncKeysApi> get(String key, boolean recursive) {
        return null;
    }

    public Future<AsyncKeysApi> getQuorum(String key) {
        return null;
    }

    public Future<AsyncKeysApi> getQuorum(String key, boolean recursive) {
        return null;
    }

    public Future<AsyncKeysApi> watch(String key) {
        return null;
    }

    public Future<AsyncKeysApi> watch(String key, boolean recursive) {
        return null;
    }

    public Future<AsyncKeysApi> watch(String key, long waitIndex) {
        return null;
    }

    public Future<AsyncKeysApi> watch(String key, boolean recursive, long waitIndex) {
        return null;
    }
}
