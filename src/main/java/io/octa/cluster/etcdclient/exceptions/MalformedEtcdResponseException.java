package io.octa.cluster.etcdclient.exceptions;

@SuppressWarnings("serial")
public class MalformedEtcdResponseException extends EtcdClientException {
    public MalformedEtcdResponseException() {
        super();
    }

    public MalformedEtcdResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MalformedEtcdResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedEtcdResponseException(String message) {
        super(message);
    }

    public MalformedEtcdResponseException(Throwable cause) {
        super(cause);
    }
}
