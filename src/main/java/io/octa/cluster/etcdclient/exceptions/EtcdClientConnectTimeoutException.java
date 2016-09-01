package io.octa.cluster.etcdclient.exceptions;

@SuppressWarnings("serial")
public class EtcdClientConnectTimeoutException extends EtcdClientTimeoutException {
    public EtcdClientConnectTimeoutException() {
        super();
    }

    public EtcdClientConnectTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EtcdClientConnectTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public EtcdClientConnectTimeoutException(String message) {
        super(message);
    }

    public EtcdClientConnectTimeoutException(Throwable cause) {
        super(cause);
    }
}
