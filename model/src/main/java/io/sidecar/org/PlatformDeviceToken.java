package io.sidecar.org;

public class PlatformDeviceToken {

    private final PlatformType platform;
    private final String deviceToken;

    public PlatformDeviceToken(PlatformType platform, String deviceToken) {
        this.platform = platform;
        this.deviceToken = deviceToken;
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

}
