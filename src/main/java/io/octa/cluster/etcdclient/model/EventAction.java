package io.octa.cluster.etcdclient.model;

public enum EventAction {
    UNKNOWN(null),
    GET("get"),
    CREATE("create"),
    SET("set"),
    UPDATE("update"),
    DELETE("delete"),
    COMPARE_AND_SWAP("compareAndSwap"),
    COMPARE_AND_DELETE("compareAndDelete"),
    EXPIRE("expire");

    private final String etcdActionText;

    private EventAction(String etcdActionText) {
        this.etcdActionText = etcdActionText;
    }

    public static EventAction fromEtcdActionText(String etcdActionText) {
        if (etcdActionText != null) {
            for (EventAction evType: values()) {
                if ((evType.etcdActionText != null) && evType.etcdActionText.equals(etcdActionText)) {
                    return evType;
                }
            }
        }

        return UNKNOWN;
    }
}
