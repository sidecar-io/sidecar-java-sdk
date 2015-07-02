package io.sidecar.org;

import com.google.common.base.Preconditions;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.UUID;

public class Application {

    private UUID orgId;
    private UUID appId;
    private String name;

    public Application(UUID orgId, String name) {
        Preconditions.checkNotNull(orgId);
        this.orgId = orgId;

        // assign the AppId
        this.appId = UUID.randomUUID();

        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(StringUtils.isNotBlank(name));
        this.name = name;
    }

    private Application(Builder builder) {
        appId = builder.appId;
        name = builder.name;
        orgId = builder.orgId;
    }

    public UUID getAppId() {
        return appId;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Application{" +
               "orgId=" + orgId +
               ", appId=" + appId +
               ", name=" + name +
               "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Application that = (Application) o;

        return
              Objects.equals(this.orgId, that.orgId) &&
              Objects.equals(this.appId, that.appId) &&
              Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, appId, name);
    }

    public static final class Builder {

        private UUID appId;
        private String name;
        private UUID orgId;

        public Builder() {
        }

        public Builder(Application copy) {
            appId = copy.appId;
            name = copy.name;
            orgId = copy.orgId;
        }

        public Builder appId(UUID appId) {
            this.appId = appId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public Application build() {
            return new Application(this);
        }
    }
}
