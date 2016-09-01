package io.octa.cluster.etcdclient.model;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class EtcdNode {
    private String key;
    private String value;
    private Boolean dir;
    private OffsetDateTime expiration;
    private Long ttlInSeconds;
    private List<EtcdNode> nodes = new ArrayList<>();
    private BigInteger modifiedIndex;
    private BigInteger createdIndex;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getDir() {
        return dir;
    }

    public void setDir(Boolean dir) {
        this.dir = dir;
    }

    public OffsetDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(OffsetDateTime expiration) {
        this.expiration = expiration;
    }

    public List<EtcdNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<EtcdNode> nodes) {
        this.nodes = nodes;
    }

    public BigInteger getModifiedIndex() {
        return modifiedIndex;
    }

    public void setModifiedIndex(BigInteger modifiedIndex) {
        this.modifiedIndex = modifiedIndex;
    }

    public BigInteger getCreatedIndex() {
        return createdIndex;
    }

    public void setCreatedIndex(BigInteger createdIndex) {
        this.createdIndex = createdIndex;
    }

    public Long getTtlInSeconds() {
        return ttlInSeconds;
    }

    public void setTtlInSeconds(Long ttlInSeconds) {
        this.ttlInSeconds = ttlInSeconds;
    }
}
