package io.octa.cluster.etcdclient.model;

import java.math.BigInteger;

public class EtcdId implements Comparable<EtcdId> {
    private final String encoded;
    private final BigInteger id;

    public EtcdId(String encodedId) {
        encoded = encodedId;
        id = new BigInteger(encodedId, 16);
    }

    public EtcdId(BigInteger id) {
        this.id = id;
        encoded = id.toString(16)
                .toLowerCase();
    }

    @Override
    public String toString() {
        return encoded + "[" + id.toString() + "]";
    }

    public String getEncodedId() {
        return encoded;
    }

    public BigInteger getId() {
        return id;
    }

    @Override
    public int compareTo(EtcdId o) {
        return o == null ? 1 : id.compareTo(o.id);
    }
}
