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
