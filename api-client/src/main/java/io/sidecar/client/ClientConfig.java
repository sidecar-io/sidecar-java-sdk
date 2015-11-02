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
