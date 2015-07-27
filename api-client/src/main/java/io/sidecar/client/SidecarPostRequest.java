package io.sidecar.client;


import static io.sidecar.util.DateUtils.nowUtc;

import java.net.URL;

import io.sidecar.jackson.ModelMapper;
import io.sidecar.security.signature.SignatureVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SidecarPostRequest extends SidecarRequest {

    private static final ModelMapper mapper = new ModelMapper();

    private static Logger LOGGER = LoggerFactory.getLogger(SidecarPostRequest.class);
    private URL url;
    private Object payload;

    @Override
    public SidecarResponse send() {
        try {
            String payloadAsJson = mapper.writeValueAsString(payload);

            LOGGER.debug("Creating sidecar POST request... " + url);
            LOGGER.debug("JSON payload is {}", payloadAsJson);

            HttpPost httpPost = new HttpPost(url.toURI());
            httpPost.setEntity(new StringEntity(payloadAsJson));

            // add the required headers for signing the request
            signHeaders(HttpPost.METHOD_NAME, url.getPath(), httpPost, payloadAsJson);

            LOGGER.debug("Sending sidecar POST request: {} ", httpPost);
            return send(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {

        private SidecarPostRequest sidecarRequest;

        public Builder(String accessKeyId, String password, String secret) {
            sidecarRequest = new SidecarPostRequest();
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
            sidecarRequest.payload = payload;
            return this;
        }

        public SidecarPostRequest build() {

            // set defaults if they are not there
            if (sidecarRequest.requestDate == null) {
                sidecarRequest.requestDate = nowUtc().toString();
            }
            return sidecarRequest;
        }
    }
}
