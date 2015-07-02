package io.sidecar.access;

public enum AccessKeyType {

    APPLICATION("Application"),
    DRONE("Drone"),
    USER("User");

    public final String keyType;

    AccessKeyType(String keyType) {
        this.keyType = keyType;
    }
}
