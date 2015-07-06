package io.sidecar.client;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

import java.net.URL;

public final class ClientConfig {

    final private URL restApiBasePath;

    public ClientConfig() {
        this(urlFromString("http://localhost:8080/rest-api"));
    }

    public ClientConfig(URL baseUrl) {
        this.restApiBasePath = checkNotNull(baseUrl);
    }

    public URL getRestApiBasePath() {
        return restApiBasePath;
    }

    private static URL urlFromString(String s) {
        try {
            return new URL(s);
        } catch (Exception e) {
            throw propagate(e);
        }
    }
}
