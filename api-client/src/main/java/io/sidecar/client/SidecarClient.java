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

import static com.google.common.base.Throwables.propagate;
import static io.sidecar.security.signature.SignatureVersion.Version.ONE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.sidecar.access.AccessKey;
import io.sidecar.credential.Credential;
import io.sidecar.event.Event;
import io.sidecar.jackson.ModelMapper;
import io.sidecar.notification.NotificationRule;
import io.sidecar.org.Device;
import io.sidecar.org.PlatformDeviceToken;
import io.sidecar.org.Role;
import io.sidecar.org.UserGroup;
import io.sidecar.org.UserGroupMember;
import io.sidecar.query.Query;
import io.sidecar.query.UserAnswerBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class SidecarClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SidecarClient.class);

    private final ClientConfig clientConfig;
    private final AccessKey accessKey;
    private final ModelMapper mapper = new ModelMapper();

    @SuppressWarnings("unused")
    public SidecarClient(AccessKey accessKey) {
        this(accessKey, new ClientConfig());
    }

    public SidecarClient(AccessKey accessKey, ClientConfig clientConfig) {
        this.accessKey = accessKey;
        this.clientConfig = clientConfig;
    }


    /**************************************
     * Authentication and Keyset Methods
     *************************************/

    /**
     * Given a username and password, obtain that user's AccessKey's for this application if that user exists.
     *
     * @param credential The user's credentials.
     * @return AccessKeys for the user identified by the provided Credentials
     */
    @SuppressWarnings("unused")
    public AccessKey authenticateUser(Credential credential) {

        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/auth");
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

    public boolean checkApplicationKeyset() {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/status");
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

    public boolean checkUserKeyset() {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/status");
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


    /**************************************
     * Event Publication Methods
     *************************************/

    public UUID postEvent(Event event) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/event");
            SidecarPostRequest sidecarPostRequest = new SidecarPostRequest.Builder(
                    accessKey.getKeyId(), "", accessKey.getSecret())
                    .withSignatureVersion(ONE)
                    .withUrl(endpoint)
                    .withPayload(event)
                    .build();
            SidecarResponse response = sidecarPostRequest.send();

            if (response.getStatusCode() == 202) {
                String s = response.getBody();
                JsonNode json = mapper.readTree(s);
                System.out.println(json);
                return UUID.fromString(json.path("id").asText());
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    /**************************************
     * User and Group Provisioning Methods
     *************************************/
    // User...
    @SuppressWarnings("unused")
    public AccessKey createNewUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/user");
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

    @SuppressWarnings("unused")
    public void deleteUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/user");
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

    @SuppressWarnings("unused")
    public Integer getUserCountForApplication() {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/user/count");
            SidecarGetRequest sidecarRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            if (response.getStatusCode() == 200) {
                return (Integer)mapper.readValue(response.getBody(), Map.class).get("count");
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public AccessKey createAccessKeyForUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/accesskey");
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

    @SuppressWarnings("unused")
    public AccessKey updateAccessKeyForUser(String emailAddress, String password) {
        try {
            Credential credential = new Credential(emailAddress, password);
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/accesskey");
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

    @SuppressWarnings("unused")
    public void updateUserMetadata(Map<String, String> metaData) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user");
            SidecarPutRequest sidecarPutRequest =
                    new SidecarPutRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(metaData)
                            .build();
            SidecarResponse response = sidecarPutRequest.send();

            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public Map<String, String> getUserMetadata() {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user");
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();

            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), Map.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public UUID userIdForUserAddress(String emailAddress) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/" + emailAddress);
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();
            if (response.getStatusCode() == 200) {
                return UUID
                        .fromString(mapper.readTree(response.getBody()).get("userId").textValue());
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    // Group....

    @SuppressWarnings("unused")
    public UserGroup createNewUserGroup(String newGroupName) {
        try {
            HashMap<String, String> payload = new HashMap<>();
            payload.put("name", newGroupName);

            URL endpoint = fullUrlForPath("/rest/v1/provision/group");
            SidecarPostRequest
                    sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(payload)
                            .build();

            SidecarResponse response = sidecarPostRequest.send();
            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), UserGroup.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public UserGroup getGroupById(UUID groupId) {
        try {

            URL endpoint = fullUrlForPath("/rest/v1/provision/group/" + groupId.toString());
            SidecarGetRequest
                    sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();

            SidecarResponse response = sidecarGetRequest.send();
            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), UserGroup.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public void deleteGroup(UUID groupId) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/group/" + groupId.toString());
            SidecarDeleteRequest sidecarRequest =
                    new SidecarDeleteRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public UserGroup addUserToUserGroup(UserGroup userGroup, UserGroupMember member) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/group/" + userGroup.getId() + "/members");
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(member)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), UserGroup.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<UUID> getGroupMembers(UUID groupId) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/group/" + groupId.toString() + "/members");
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();

            if (response.getStatusCode() == 200) {
                return Collections.checkedList(mapper.readValue(response.getBody(), List.class), UUID.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public UserGroup removeMemberFromGroup(UserGroup group, UUID userId) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/group/" + group.getId().toString() + "/members/" +
                    userId.toString());
            SidecarDeleteRequest sidecarDeleteRequest =
                    new SidecarDeleteRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarDeleteRequest.send();

            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), UserGroup.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public UserGroup changeRoleForGroupMember(UserGroup group, UUID userId, Role newRole) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/group/" + group.getId().toString() + "/members/" +
                    userId.toString() + "/role");
            ObjectNode roleJson = JsonNodeFactory.instance.objectNode();
            roleJson.put("role", newRole.roleName);
            SidecarPutRequest sidecarPutRequest =
                    new SidecarPutRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(roleJson)
                            .build();
            SidecarResponse response = sidecarPutRequest.send();

            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), UserGroup.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }


    @SuppressWarnings({"unchecked", "unused"})
    public List<UUID> getGroupsForUser(String emailAddress) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/" + emailAddress + "/groups");
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();

            if (response.getStatusCode() == 200) {
                return Collections.checkedList(mapper.readValue(response.getBody(), List.class), UUID.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }


    /**************************************
     * Device Provisioning Methods
     *************************************/

    @SuppressWarnings("unused")
    public void provisionDevice(UUID deviceId) {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("deviceId", deviceId.toString());
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/device");
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(metaData)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public void deprovisionDevice(UUID deviceId) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/device/" + deviceId.toString());
            SidecarDeleteRequest sidecarDeleteRequest =
                    new SidecarDeleteRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarDeleteRequest.send();

            //The response body is empty if 204, so only check if we do not have a 204 response
            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public Integer getDeviceCountForApplication() {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/application/device/count");
            SidecarGetRequest sidecarRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(endpoint)
                            .withSignatureVersion(ONE)
                            .build();
            SidecarResponse response = sidecarRequest.send();
            if (response.getStatusCode() == 200) {
                return (Integer)mapper.readValue(response.getBody(), Map.class).get("count");
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public Map<String, String> getUserDeviceMetadata(UUID deviceId) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/device/" + deviceId.toString());
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();

            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), Map.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public void updateUserDeviceMetadata(UUID deviceId, Map<String, String> metaData) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/device/" + deviceId.toString());
            SidecarPutRequest sidecarPutRequest =
                    new SidecarPutRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(metaData)
                            .build();
            SidecarResponse response = sidecarPutRequest.send();

            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings({"unused", "unchecked"})
    public List<Device> getDevicesForUser() {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/devices");
            SidecarGetRequest sidecarPostRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            // check for an OK response code
            if (response.getStatusCode() == 200) {
                return Collections.checkedList(mapper.readValue(response.getBody(), java.util.List.class), Device.class);

            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }


    /********************************************
     * Notification and Notification Rule Methods
     ********************************************/


    @SuppressWarnings("unused")
    public void addNotificationToken(PlatformDeviceToken token) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/notifications/token");
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(token)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            // check for an no content response code
            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public UUID addNotificationRule(String name, String description, String stream, String key, Double min, Double max) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("stream", stream);
        payload.put("key", key);
        payload.put("min", min);
        payload.put("max", max);

        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/notifications/rule");
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(payload)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            // check for an OK response code
            if (response.getStatusCode() == 200) {
                return UUID.fromString(mapper.readTree(response.getBody()).get("id").textValue());
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public UUID updateNotificationRule(UUID ruleId, String name, String description, String stream, String key, Double min, Double max) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("stream", stream);
        payload.put("key", key);
        payload.put("min", min);
        payload.put("max", max);

        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/notifications/rule/" + ruleId.toString());
            SidecarPutRequest sidecarPutRequest =
                    new SidecarPutRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(payload)
                            .build();
            SidecarResponse response = sidecarPutRequest.send();

            // check for an OK response code
            if (response.getStatusCode() == 200) {
                return UUID.fromString(mapper.readTree(response.getBody()).get("id").textValue());
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public NotificationRule getNotificationRule(UUID ruleId) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/notifications/rule/" + ruleId.toString());
            SidecarGetRequest sidecarPostRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            // check for an OK response code
            if (response.getStatusCode() == 200) {
                return mapper.readValue(response.getBody(), NotificationRule.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings({"unused", "unchecked"})
    public List<NotificationRule> getNotificationRules() {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/notifications/rules");
            SidecarGetRequest sidecarPostRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            // check for an OK response code
            if (response.getStatusCode() == 200) {
                return Collections.checkedList(mapper.readValue(response.getBody(), java.util.List.class), NotificationRule.class);

            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unused")
    public void deleteNotificationRule(UUID ruleId) {
        try {
            URL endpoint = fullUrlForPath("/rest/v1/provision/user/notifications/rule/" + ruleId.toString());
            SidecarDeleteRequest sidecarDeleteRequest =
                    new SidecarDeleteRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarDeleteRequest.send();

            // check for an no content response code
            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    /*************************************
     * Query Methods
     *************************************/

    @SuppressWarnings("unused")
    public List<UserAnswerBucket> postUserQuery(String type, Query query) {
        String path = "/rest/v1/query/user/devices/" + type;
        return postUserBasedQuery(path, query);
    }

    public List<UserAnswerBucket> postUserDeviceQuery(String type, UUID deviceId, Query query) {
        String path = "/rest/v1/query/user/device/" + deviceId.toString() + "/" + type;
        return postUserBasedQuery(path, query);
    }

    @SuppressWarnings("unchecked")
    private List<UserAnswerBucket> postUserBasedQuery(String path, Query query) {
        try {
            URL endpoint = fullUrlForPath(path);
            SidecarPostRequest sidecarPostRequest = new SidecarPostRequest.Builder(
                    accessKey.getKeyId(), "", accessKey.getSecret())
                    .withSignatureVersion(ONE)
                    .withUrl(endpoint)
                    .withPayload(query)
                    .build();
            SidecarResponse response = sidecarPostRequest.send();

            if (response.getStatusCode() == 200) {
                LOGGER.debug(response.getBody());
                return Collections.checkedList(mapper.readValue(response.getBody(), List.class), UserAnswerBucket.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }


    public ClientConfig getConfig() {
        return clientConfig;
    }

    private URL fullUrlForPath(String path) {
        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            return new URL(baseUrl.toString() + path);
        } catch (MalformedURLException e) {
            throw propagate(e);
        }
    }


}
