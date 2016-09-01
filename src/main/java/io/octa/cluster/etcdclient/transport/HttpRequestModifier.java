package io.octa.cluster.etcdclient.transport;

public interface HttpRequestModifier {
    public static enum HttpMethod {
        HEAD,
        GET,
        PUT,
        POST,
        DELETE;
    }

    public HttpRequestModifier setPath(String... pathElements);

    public HttpRequestModifier setMethod(HttpMethod method);

    public HttpRequestModifier addQueryParam(String name, String value);

    public HttpRequestModifier addPostParam(String name, String value);

    public HttpRequestModifier setBody(byte[] bodyData);

    public HttpRequestModifier setWaitCommand();
}
