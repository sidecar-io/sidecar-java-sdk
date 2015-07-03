package io.sidecar.security.signature;

import org.apache.commons.lang.StringUtils;

public class SignatureVersionOne extends SignatureVersion {

    private String contentMd5;
    private String dateString;
    private String method;
    private String uri;

    @Override
    protected Version getVersion() {
        return Version.ONE;
    }

    @Override
    public String getStringToSign() {
        // order matters! method + uri + date + md5(opt) + version
        StringBuilder s = new StringBuilder();
        s.append(method).append(DELIMITER);
        s.append(uri).append(DELIMITER);
        s.append(dateString).append(DELIMITER);
        // only applies to PUT or POST
        if (StringUtils.isNotEmpty(contentMd5)) {
            s.append(contentMd5).append(DELIMITER);
        }
        return s.append(getVersion().digit).toString();
    }

    public static class Builder {

        public SignatureVersionOne signatureVersionOne;

        public Builder() {
            signatureVersionOne = new SignatureVersionOne();
        }

        public Builder withDate(String dateString) {
            signatureVersionOne.dateString = dateString;
            return this;
        }

        public Builder withContentMd5(String contentMd5) {
            signatureVersionOne.contentMd5 = contentMd5;
            return this;
        }

        /*
         * the URI path, ie "/event" -- or everything between hostname and "?" in the URL
         */
        public Builder withURI(String uri) {
            signatureVersionOne.uri = uri;
            return this;
        }

        public Builder withHttpMethod(String httpMethod) {
            signatureVersionOne.method = httpMethod;
            return this;
        }

        public SignatureVersionOne build() {
            return signatureVersionOne;
        }
    }
}
