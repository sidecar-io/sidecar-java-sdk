package io.sidecar.org;

public enum UserStatus {

    INACTIVE(0),
    ACTIVE(1);

    private final int value;

    private UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
