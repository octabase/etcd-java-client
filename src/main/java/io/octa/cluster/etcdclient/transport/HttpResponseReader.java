package io.octa.cluster.etcdclient.transport;

import java.util.List;

public interface HttpResponseReader {
    public List<String> getHeader(String headerName);

    public byte[] getBody();
}
