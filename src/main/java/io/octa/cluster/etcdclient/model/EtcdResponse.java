package io.octa.cluster.etcdclient.model;

public class EtcdResponse<T> {
    private T payload;
    private EtcdId clusterId;

    public EtcdId getClusterId() {
        return clusterId;
    }

    public void setClusterId(EtcdId clusterId) {
        this.clusterId = clusterId;
    }

    public T getPayload() {
        return this.payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
