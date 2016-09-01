package io.octa.cluster.etcdclient.transport;

import java.util.concurrent.CompletableFuture;

import io.octa.cluster.etcdclient.command.EtcdCommand;

public interface EtcdTransport extends AutoCloseable {
    public <T> T execute(EtcdCommand<T> command);

    public <T> CompletableFuture<T> executeAsync(EtcdCommand<T> command);
}
