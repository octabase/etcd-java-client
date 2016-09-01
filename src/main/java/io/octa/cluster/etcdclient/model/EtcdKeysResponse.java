package io.octa.cluster.etcdclient.model;

import java.math.BigInteger;
import java.util.List;

public class EtcdKeysResponse extends EtcdResponse<EtcdNode> {
    private BigInteger etcdIndex;
    private BigInteger raftIndex;
    private BigInteger raftTerm;

    private EventAction action;
    private EtcdNode prevNode;
    private Boolean refresh;

    public String asValue() {
        EtcdNode node = getPayload();

        if (node == null) {
            return null;
        }

        if (node.getDir() == null) {
            throw new IllegalArgumentException();
        }

        if (node.getDir() == false) {
            return node.getValue();
        }

        throw new IllegalArgumentException();
    }

    public List<EtcdNode> asDirectory() {
        EtcdNode node = getPayload();

        if (node == null) {
            return null;
        }

        if (node.getDir() == null) {
            throw new IllegalArgumentException();
        }

        if (node.getDir() == true) {
            return node.getNodes();
        }

        throw new IllegalArgumentException();
    }

    public BigInteger getEtcdIndex() {
        return etcdIndex;
    }

    public void setEtcdIndex(BigInteger etcdIndex) {
        this.etcdIndex = etcdIndex;
    }

    public BigInteger getRaftIndex() {
        return raftIndex;
    }

    public void setRaftIndex(BigInteger raftIndex) {
        this.raftIndex = raftIndex;
    }

    public BigInteger getRaftTerm() {
        return raftTerm;
    }

    public void setRaftTerm(BigInteger raftTerm) {
        this.raftTerm = raftTerm;
    }

    public EventAction getAction() {
        return action;
    }

    public void setAction(EventAction action) {
        this.action = action;
    }

    public EtcdNode getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(EtcdNode prevNode) {
        this.prevNode = prevNode;
    }

    public Boolean getRefresh() {
        return refresh;
    }

    public void setRefresh(Boolean refresh) {
        this.refresh = refresh;
    }
}
