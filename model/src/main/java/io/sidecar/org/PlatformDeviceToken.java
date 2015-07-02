package io.sidecar.org;

public class PlatformDeviceToken {

    private final String platform;
    private final String deviceToken;

    public PlatformDeviceToken(String platform, String deviceToken) {
        this.platform = platform;
        this.deviceToken = deviceToken;
    }

    private PlatformDeviceToken(Builder builder) {
        platform = builder.platform;
        deviceToken = builder.deviceToken;
    }

    public String getPlatform() {
        return platform;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public static final class Builder {

        private String platform;
        private String deviceToken;

        public Builder() {
        }

        public Builder(PlatformDeviceToken copy) {
            platform = copy.platform;
            deviceToken = copy.deviceToken;
        }

        public Builder platform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder deviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
            return this;
        }


        public PlatformDeviceToken build() {
            return new PlatformDeviceToken(this);
        }
    }
}
