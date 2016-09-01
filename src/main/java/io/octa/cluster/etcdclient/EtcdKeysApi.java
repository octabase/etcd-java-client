package io.octa.cluster.etcdclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.octa.cluster.etcdclient.model.EtcdKeysResponse;

public class EtcdKeysApi {
    private EtcdKeysAsyncApi async;

    EtcdKeysApi(EtcdKeysAsyncApi async) {
        this.async = async;
    }

    private EtcdKeysResponse sync(CompletableFuture<EtcdKeysResponse> event) {
        try {
            return event.get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        } catch (ExecutionException e) {
            Throwable cause = e.getCause();

            if (cause != null) {
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }

                throw new RuntimeException(cause);
            }

            throw new RuntimeException(e);
        }
    }

    public EtcdKeysAsyncApi async() {
        return async;
    }

    public EtcdKeysResponse set(String key, String value) {
        return sync(async.set(key, value));
    }

    public EtcdKeysResponse set(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.set(key, value, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse compareAndSwap(String key, String expectedValue, String value) {
        return sync(async.compareAndSwap(key, expectedValue, value));
    }

    public EtcdKeysResponse compareAndSwap(String key, String expectedValue, String value, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.compareAndSwap(key, expectedValue, value, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse compareAndSwap(String key, long expectedModifiedIndex, String value) {
        return sync(async.compareAndSwap(key, expectedModifiedIndex, value));
    }

    public EtcdKeysResponse compareAndSwap(String key, long expectedModifiedIndex, String value, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.compareAndSwap(key, expectedModifiedIndex, value, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse setIfExists(String key, String value) {
        return sync(async.setIfExists(key, value));
    }

    public EtcdKeysResponse setIfExists(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.setIfExists(key, value, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse setIfNotExists(String key, String value) {
        return sync(async.setIfNotExists(key, value));
    }

    public EtcdKeysResponse setIfNotExists(String key, String value, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.setIfNotExists(key, value, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse delete(String key) {
        return sync(async.delete(key));
    }

    public EtcdKeysResponse deleteDirectory(String key) {
        return sync(async.deleteDirectory(key));
    }

    public EtcdKeysResponse compareAndDelete(String key, String expectedValue) {
        return sync(async.compareAndDelete(key, expectedValue));
    }

    public EtcdKeysResponse compareAndDelete(String key, long expectedModifiedIndex) {
        return sync(async.compareAndDelete(key, expectedModifiedIndex));
    }

    public EtcdKeysResponse refresh(String key, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.refresh(key, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse createDirectory(String key) {
        return sync(async.createDirectory(key));
    }

    public EtcdKeysResponse createDirectory(String key, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.createDirectory(key, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse refreshIfExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.refreshIfExists(key, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse refreshIfNotExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.refreshIfNotExists(key, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse refreshDirectory(String key, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.refreshDirectory(key, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse refreshDirectoryIfExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.refreshDirectoryIfExists(key, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse refreshDirectoryIfNotExists(String key, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.refreshDirectoryIfNotExists(key, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse createByInOrderKey(String parentKey, String value) {
        return sync(async.createByInOrderKey(parentKey, value));
    }

    public EtcdKeysResponse createByInOrderKey(String parentKey, String value, long ttl, TimeUnit ttlTimeUnit) {
        return sync(async.createByInOrderKey(parentKey, value, ttl, ttlTimeUnit));
    }

    public EtcdKeysResponse get(String key) {
        return sync(async.get(key));
    }

    public EtcdKeysResponse get(String key, boolean recursive) {
        return sync(async.get(key, recursive));
    }

    public EtcdKeysResponse getQuorum(String key) {
        return sync(async.getQuorum(key));
    }

    public EtcdKeysResponse getQuorum(String key, boolean recursive) {
        return sync(async.getQuorum(key, recursive));
    }

    public EtcdKeysResponse watch(String key) {
        return sync(async.watch(key));
    }

    public EtcdKeysResponse watch(String key, boolean recursive) {
        return sync(async.watch(key, recursive));
    }

    public EtcdKeysResponse watch(String key, long waitIndex) {
        return sync(async.watch(key, waitIndex));
    }

    public EtcdKeysResponse watch(String key, boolean recursive, long waitIndex) {
        return sync(async.watch(key, recursive, waitIndex));
    }
}
