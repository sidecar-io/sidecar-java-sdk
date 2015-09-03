package io.sidecar.client;

import static com.google.common.base.Throwables.propagate;
import static io.sidecar.util.DateUtils.nowUtc;

import java.net.URL;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import io.sidecar.jackson.ModelMapper;
import io.sidecar.security.signature.SignatureVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SidecarDeleteRequest extends SidecarRequest {
    private static final ModelMapper mapper = new ModelMapper();

    private static Logger LOGGER = LoggerFactory.getLogger(SidecarPostRequest.class);
    private URL url;
    private Object payload;

    @Override
    public SidecarResponse send() {
        try {
            String payloadAsJson = mapper.writeValueAsString(payload);

            LOGGER.debug("Creating sidecar DELETE request... " + url);
            LOGGER.debug("JSON_MEDIA_TYPE payload is {}", payloadAsJson);

            Request.Builder request = new Request.Builder()
                    .url(url)
                    .delete(RequestBody.create(JSON_MEDIA_TYPE, payloadAsJson));

            // add the required headers for signing the request
            signHeaders("DELETE", url.getPath(), request, payloadAsJson);
            return send(request.build());
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public static class Builder {

        private SidecarDeleteRequest sidecarRequest;

        public Builder(String accessKeyId, String password, String secret) {
            sidecarRequest = new SidecarDeleteRequest();
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

        public SidecarDeleteRequest build() {

            // set defaults if they are not there
            if (sidecarRequest.requestDate == null) {
                sidecarRequest.requestDate = nowUtc().toString();
            }
            return sidecarRequest;
        }
    }}
