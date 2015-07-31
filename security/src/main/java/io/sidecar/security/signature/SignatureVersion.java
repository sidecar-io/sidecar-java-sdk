package io.sidecar.security.signature;

public abstract class SignatureVersion {

    static final String DELIMITER = "\n";

    protected abstract Version getVersion();

    public abstract String getStringToSign();

    public enum Version {
        ONE("1");

        public final String digit;

        Version(String digit) {
            this.digit = digit;
        }

        @SuppressWarnings("unused")
        public static Version fromString(String text) {
            if (text == null) {
                return null;
            }
            for (Version v : Version.values()) {
                if (text.trim().equalsIgnoreCase(v.digit)) {
                    return v;
                }
            }
            return null;
        }
    }
}
