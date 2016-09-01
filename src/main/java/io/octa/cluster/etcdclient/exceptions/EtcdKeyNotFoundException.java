package io.octa.cluster.etcdclient.exceptions;

@SuppressWarnings("serial")
public class EtcdKeyNotFoundException extends EtcdRemoteException {
    public EtcdKeyNotFoundException(EtcdRemoteException cause, String message) {
        super(message, cause);

        setEtcdErrorCode(cause.getEtcdErrorCode());
        setEtcdErrorMessage(cause.getMessage());
        setEtcdErrorCause(cause.getEtcdErrorCause());
    }
}
