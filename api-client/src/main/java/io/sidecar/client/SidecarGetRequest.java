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

package io.sidecar.client;

import com.squareup.okhttp.Request;
import io.sidecar.security.signature.SignatureVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static io.sidecar.util.DateUtils.nowUtc;

class SidecarGetRequest extends SidecarRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SidecarGetRequest.class);

    private URL url;

    @Override
    SidecarResponse send() {
        LOGGER.debug("Creating sidecar GET request..." + url);
        Request.Builder req = new Request.Builder().url(url);

        signHeaders("GET", url.getPath(), req);

        return send(req.build());
    }

    public static class Builder {

        private SidecarGetRequest sidecarRequest;

        public Builder(String accessKeyId, String password, String secret) {
            sidecarRequest = new SidecarGetRequest();
            sidecarRequest.secret = secret;
            sidecarRequest.accessKey = accessKeyId;
            sidecarRequest.password = password;
        }

        public Builder withSignatureVersion(SignatureVersion.Version version) {
            sidecarRequest.signatureVersion = version.digit;
            return this;
        }

        public Builder withUrl(URL url) {
            sidecarRequest.url = url;
            return this;
        }

        public SidecarGetRequest build() {

            // set defaults if they are not there
            if (sidecarRequest.requestDate == null) {
                sidecarRequest.requestDate = nowUtc().toString();
            }
            return sidecarRequest;
        }
    }
}
