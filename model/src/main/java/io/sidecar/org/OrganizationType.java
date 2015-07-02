package io.sidecar.org;

public enum OrganizationType {

    FREE("Free"),
    SPECIAL("Premium"),
    FANCY("Enterprise");

    public final String orgTypeName;

    OrganizationType(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

}
