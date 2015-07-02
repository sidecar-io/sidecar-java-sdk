package io.sidecar.access;

/**
 * Used during authentication by Shiro and during Provisioning
 */
public final class AccessKey {

    private final String secret;
    private final String keyId;

    private AccessKey(String keyId, String secret) {
        this.keyId = keyId;
        this.secret = secret;
    }

    private AccessKey(Builder builder) {
        this(builder.keyId, builder.secret);
    }

    public String getSecret() {
        return secret;
    }

    public String getKeyId() {
        return keyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccessKey accessKey = (AccessKey) o;

        if (keyId != null ? !keyId.equals(accessKey.keyId) : accessKey.keyId != null) {
            return false;
        }
        if (secret != null ? !secret.equals(accessKey.secret) : accessKey.secret != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = secret != null ? secret.hashCode() : 0;
        result = 31 * result + (keyId != null ? keyId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccessKey{" +
               "secret='" + secret + '\'' +
               ", keyId='" + keyId + '\'' +
               '}';
    }

    public static final class Builder {

        private String keyId;
        private String secret;

        public Builder() {
        }

        public Builder(AccessKey copy) {
            keyId = copy.keyId;
            secret = copy.secret;
        }


        public Builder keyId(String keyId) {
            this.keyId = keyId;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public AccessKey build() {
            return new AccessKey(this);
        }
    }
}
