package io.sidecar.client;

import static io.sidecar.util.DateUtils.nowUtc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sidecar.jackson.ModelMapper;
import io.sidecar.security.signature.SignatureVersion;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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
            LOGGER.debug("JSON payload is {}", payloadAsJson);

            HttpPut httpPut = new HttpPut(url.toURI());
            httpPut.setEntity(new StringEntity(payloadAsJson));

            // add the required headers for signing the request
            signHeaders(HttpPut.METHOD_NAME, url.getPath(), httpPut, payloadAsJson);

            LOGGER.debug("Sending sidecar PUT request: {} ", httpPut);
            return send(httpPut);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

        public Builder withPayload(Object event) {
            sidecarRequest.event = event;
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
