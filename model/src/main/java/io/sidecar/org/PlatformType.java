package io.sidecar.org;

public enum PlatformType {

    EMAIL("Email"),
    APNS("Apple Push"),
    APNS_SANDBOX("Apple Push Sandbox"),
    GCM("Android Push"),
    MPNS("Microsoft Push"),
    WNS("Windows Push"),
    BAIDU("Baidu Push"),
    WEBHOOK("Webhook");

    public final String notificationType;

    PlatformType(String notificationType) {
        this.notificationType = notificationType;
    }
}
