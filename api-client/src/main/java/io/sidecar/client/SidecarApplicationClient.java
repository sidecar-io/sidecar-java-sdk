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

import static com.google.common.base.Throwables.propagate;
import static io.sidecar.security.signature.SignatureVersion.Version.ONE;

import io.sidecar.access.AccessKey;
import io.sidecar.credential.Credential;
import io.sidecar.jackson.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;


/**
 * A Sidecar API Client used to access functionality on behalf of an Application.  This client requires a set of valid
 * and active AccessKeys for a given Application.  This client is used to perform basic API management of users and to
 * access a limited number of statistics regarding the Application
 */
@SuppressWarnings("unused")
public class SidecarApplicationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SidecarUserClient.class);

    private final ClientConfig clientConfig;
    private final AccessKey accessKey;
    private final ModelMapper mapper = new ModelMapper();

    public SidecarApplicationClient(AccessKey accessKey) {
        this(accessKey, new ClientConfig());
    }

    public SidecarApplicationClient(AccessKey accessKey, ClientConfig clientConfig) {
        this.accessKey = accessKey;
        this.clientConfig = clientConfig;
    }

    /**
     * Authenticate a user with a set of credentials.  If the credentials are valid, then an AccessKey is returned that
     * may be used in a {@link SidecarUserClient} to make API requests on that User's behalf.
     *
     * @param credential The user's credentials
     * @return AccessKeys for the user identified by the provided Credentials
     */
    public AccessKey authenticateUser(Credential credential) {

        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/auth");
            SidecarPostRequest sidecarPostRequest = new SidecarPostRequest.Builder(
                    accessKey.getKeyId(), "", accessKey.getSecret())
                    .withSignatureVersion(ONE)
                    .withUrl(endpoint)
                    .withPayload(credential)
                    .build();
            SidecarResponse response = sidecarPostRequest.send();

            if (response.getStatusCode() == 200) {
                LOGGER.debug(response.getBody());
                return mapper.readValue(response.getBody(), AccessKey.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }

        } catch (Exception e) {
            throw propagate(e);
        }
    }

    /**
     * This is a convenience method to determine whether the AccessKey instance used to initialize this client is valid.
     * A RuntimeException will be thrown from this method if communication to the Sidecar platform cannot be made.
     *
     * @return true if authentication with the passed in Keyset authenticates correctly,  false if not
     */
    public boolean isClientAuthenticated() {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/status");
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();

            return response.getStatusCode() == 200;
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public AccessKey createNewUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/user");
            SidecarPostRequest sidecarRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .withPayload(credential)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), AccessKey.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public void deleteUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/user");
            SidecarDeleteRequest sidecarRequest =
                    new SidecarDeleteRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .withPayload(credential)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            // check for an no content response code
            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public int getUserCountForApplication() {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/user/count");
            SidecarGetRequest sidecarRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            if (response.getStatusCode() == 200) {
                return mapper.readTree(response.getBody()).path("count").asInt();
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public int getDeviceCountForApplication() {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/device/count");
            SidecarGetRequest sidecarRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            if (response.getStatusCode() == 200) {
                return mapper.readTree(response.getBody()).path("count").asInt();
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public AccessKey createAccessKeyForUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/accesskey");
            SidecarPostRequest sidecarRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .withPayload(credential)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), AccessKey.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public AccessKey updateAccessKeyForUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/application/accesskey");
            SidecarPutRequest sidecarPutRequest =
                    new SidecarPutRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(credential)
                            .build();
            SidecarResponse response = sidecarPutRequest.send();

            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), AccessKey.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }
}
