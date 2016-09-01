package io.octa.cluster.etcdclient.exceptions;

@SuppressWarnings("serial")
public class EtcdClientException extends RuntimeException {
    public EtcdClientException() {
        super();
    }

    public EtcdClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EtcdClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public EtcdClientException(String message) {
        super(message);
    }

    public EtcdClientException(Throwable cause) {
        super(cause);
    }
}
