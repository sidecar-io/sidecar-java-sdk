/*
 * Copyright 2015 Sidecar.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sidecar.security.signature;

import org.apache.commons.lang.StringUtils;

public class SignatureVersionOne extends SignatureVersion {

    private final String contentMd5;
    private final String dateString;
    private final String method;
    private final String uri;

    private SignatureVersionOne(Builder b) {
        contentMd5 = b.contentMd5;
        dateString = b.dateString;
        method = b.method;
        uri = b.uri;
    }

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

        private String contentMd5;
        private String dateString;
        private String method;
        private String uri;

        public Builder() {

        }

        public Builder withDate(String dateString) {
            this.dateString = dateString;
            return this;
        }

        public Builder withContentMd5(String contentMd5) {
            this.contentMd5 = contentMd5;
            return this;
        }

        /*
         * the URI path, ie "/event" -- or everything between hostname and "?" in the URL
         */
        public Builder withURI(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder withHttpMethod(String httpMethod) {
            this.method = httpMethod;
            return this;
        }

        public SignatureVersionOne build() {
            return new SignatureVersionOne(this);
        }
    }
}
