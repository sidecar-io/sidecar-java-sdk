package io.sidecar.client;

import io.sidecar.security.signature.SignatureVersion;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static io.sidecar.util.DateUtils.nowUtc;

public class SidecarGetRequest extends SidecarRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SidecarGetRequest.class);

    private URI uri;

    @Override
    SidecarResponse send() {
        LOGGER.debug("Creating sidecar GET request..." + uri);
        HttpGet httpGet = new HttpGet(uri);

        // add the required headers
        signHeaders(HttpGet.METHOD_NAME, uri.getPath(), httpGet);

        return send(httpGet);
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
            try {
                sidecarRequest.uri = url.toURI();
                return this;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
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
