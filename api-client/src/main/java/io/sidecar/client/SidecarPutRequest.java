/*
 * Copyright 2015 QSense, Inc.
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

import static io.sidecar.util.DateUtils.nowUtc;

import com.google.common.base.Throwables;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import io.sidecar.jackson.ModelMapper;
import io.sidecar.security.signature.SignatureVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

@SuppressWarnings("unused")
 class SidecarPutRequest extends SidecarRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SidecarPostRequest.class);
    private static final ModelMapper mapper = new ModelMapper();

    private URL url;
    private Object event;

    @Override
    public SidecarResponse send() {
        try {
            String payloadAsJson = mapper.writeValueAsString(event);

            LOGGER.debug("Creating sidecar PUT request... " + url);
            LOGGER.debug("JSON_MEDIA_TYPE payload is {}", payloadAsJson);

            Request.Builder request = new Request.Builder()
                    .url(url)
                    .put(RequestBody.create(JSON_MEDIA_TYPE, payloadAsJson));

            // add the required headers for signing the request
            signHeaders("PUT", url.getPath(), request, payloadAsJson);

            return send(request.build());
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static class Builder {

        private SidecarPutRequest sidecarRequest;

        public Builder(String accessKeyId, String password, String secret) {
            sidecarRequest = new SidecarPutRequest();
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

        public Builder withPayload(Object payload) {
            sidecarRequest.event = payload;
            return this;
        }

        public SidecarPutRequest build() {
            // set defaults if they are not there
            if (sidecarRequest.requestDate == null) {
                sidecarRequest.requestDate = nowUtc().toString();
            }
            return sidecarRequest;
        }
    }
}
