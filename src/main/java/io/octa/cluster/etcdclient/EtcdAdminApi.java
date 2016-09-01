package io.octa.cluster.etcdclient;

public class EtcdAdminApi {
    private final EtcdClient client;

    EtcdAdminApi(EtcdClient client) {
        this.client = client;
    }

    public boolean isHealthy() {
        return false;
    }
}
