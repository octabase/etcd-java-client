package io.octa.cluster.etcdclient.exceptions;

@SuppressWarnings("serial")
public class EtcdClientReadTimeoutException extends EtcdClientTimeoutException {
    public EtcdClientReadTimeoutException() {
        super();
    }

    public EtcdClientReadTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EtcdClientReadTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public EtcdClientReadTimeoutException(String message) {
        super(message);
    }

    public EtcdClientReadTimeoutException(Throwable cause) {
        super(cause);
    }
}
