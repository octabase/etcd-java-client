package io.octa.cluster.etcdclient.exceptions;

@SuppressWarnings("serial")
public class EtcdClientTimeoutException extends EtcdClientException {
    public EtcdClientTimeoutException() {
        super();
    }

    public EtcdClientTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EtcdClientTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public EtcdClientTimeoutException(String message) {
        super(message);
    }

    public EtcdClientTimeoutException(Throwable cause) {
        super(cause);
    }
}
