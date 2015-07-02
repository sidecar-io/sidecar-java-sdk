package io.sidecar.org;

public enum Role {

    SYSTEM("System"),
    ADMIN("Administrator"),
    DEV("Developer");

    public final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

}
