package io.octa.cluster.etcdclient.exceptions;

@SuppressWarnings("serial")
public class EtcdRemoteException extends EtcdClientTimeoutException {
    private int etcdErrorCode = -1;
    private String etcdErrorMessage;
    private String etcdErrorCause;

    public EtcdRemoteException() {
        super();
    }

    public EtcdRemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EtcdRemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public EtcdRemoteException(String message) {
        super(message);
    }

    public EtcdRemoteException(Throwable cause) {
        super(cause);
    }

    public int getEtcdErrorCode() {
        return etcdErrorCode;
    }

    public void setEtcdErrorCode(int etcdErrorCode) {
        this.etcdErrorCode = etcdErrorCode;
    }

    public String getEtcdErrorMessage() {
        return etcdErrorMessage;
    }

    public void setEtcdErrorMessage(String etcdErrorMessage) {
        this.etcdErrorMessage = etcdErrorMessage;
    }

    public String getEtcdErrorCause() {
        return etcdErrorCause;
    }

    public void setEtcdErrorCause(String etcdErrorCause) {
        this.etcdErrorCause = etcdErrorCause;
    }
}
