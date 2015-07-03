package io.sidecar.client;

import static com.google.common.base.Throwables.propagate;

import java.net.MalformedURLException;
import java.net.URL;

public final class ClientConfig {

    final private URL restApiBasePath;

    public ClientConfig() {
        this(new Builder());
    }

    private ClientConfig(Builder bd) {
        restApiBasePath = getOrDefault(bd.restApiBasePath, "http://localhost:8080/rest-api");
    }

    private URL getOrDefault(URL builderUrl, String defaultUrl) {
        try {
            return (builderUrl != null) ? builderUrl : new URL(defaultUrl);
        } catch (MalformedURLException e) {
            throw propagate(e);
        }
    }

    public URL getRestApiBasePath() {
        return restApiBasePath;
    }

    public static final class Builder {

        private URL restApiBasePath;

        public ClientConfig build() {
            return new ClientConfig(this);
        }

        public Builder withRestApiBasePath(URL basePath) {
            this.restApiBasePath = basePath;
            return this;
        }
    }
}
