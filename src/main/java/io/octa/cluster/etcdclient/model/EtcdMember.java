package io.octa.cluster.etcdclient.model;

import java.util.List;

public class EtcdMember {
    private EtcdId id;
    private String name;
    private List<String> peerURLs;
    private List<String> clientURLs;

    public EtcdId getId() {
        return id;
    }

    public void setId(EtcdId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPeerURLs() {
        return peerURLs;
    }

    public void setPeerURLs(List<String> peerURLs) {
        this.peerURLs = peerURLs;
    }

    public List<String> getClientURLs() {
        return clientURLs;
    }

    public void setClientURLs(List<String> clientURLs) {
        this.clientURLs = clientURLs;
    }

    @Override
    public String toString() {
        return "EtcdMember [id=" + id + ", name=" + name + ", peerURLs=" + peerURLs + ", clientURLs=" + clientURLs + "]";
    }
}
