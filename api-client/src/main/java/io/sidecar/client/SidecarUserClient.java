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

import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Throwables;
import io.sidecar.access.AccessKey;
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

/**
 * A Sidecar API Client used to access functionality on behalf of a User.  This client requires a set of valid
 * and active AccessKeys for a given User.
 */
@SuppressWarnings("unused")
public class SidecarUserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SidecarUserClient.class);

    private final ClientConfig clientConfig;
    private final AccessKey accessKey;
    private final ModelMapper mapper = new ModelMapper();

    public SidecarUserClient(AccessKey accessKey) {
        this(accessKey, new ClientConfig());
    }

    public SidecarUserClient(AccessKey accessKey, ClientConfig clientConfig) {
        this.accessKey = accessKey;
        this.clientConfig = clientConfig;
    }


    /**************************************
     * Authentication
     *************************************/
    public boolean checkUserKeyset() {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/status");
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
     * Event Publication
     *************************************/
    public UUID postEvent(Event event) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/event");
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
     * User and Group Provisioning
     *************************************/
    public void updateUserPassword(String currentPassword, String newPassword) {
        try {
            Map<String, String> info = new HashMap<>();
            info.put("currentPassword", currentPassword);
            info.put("newPassword", newPassword);
            String payloadAsJason = mapper.writeValueAsString(info);
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/password");
            SidecarPutRequest sidecarPutRequest =
                    new SidecarPutRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .withPayload(payloadAsJason)
                            .build();
            SidecarResponse response = sidecarPutRequest.send();

            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public void updateUserMetadata(Map<String, String> metaData) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user");
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

    @SuppressWarnings("unchecked")
    public Map<String, String> getUserMetadata() {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user");
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();

            if (response.getStatusCode() == 200) {
                return Collections.checkedMap(mapper.readValue(response.getBody(), Map.class),
                        String.class, String.class);

            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public UUID userIdForUserAddress(String emailAddress) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/" + encode(emailAddress));
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

    public UserGroup createNewUserGroup(String newGroupName) {
        try {
            HashMap<String, String> payload = new HashMap<>();
            payload.put("name", newGroupName);

            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/group");
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

    public UserGroup getGroupById(UUID groupId) {
        try {

            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/group/" + encode(groupId.toString()));
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

    public void deleteGroup(UUID groupId) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/group/" + encode(groupId.toString()));
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

    public UserGroup addUserToUserGroup(UserGroup userGroup, UserGroupMember member) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/group/" +
                    encode(userGroup.getId().toString()) + "/members");
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

    @SuppressWarnings("unchecked")
    public List<UUID> getGroupMembers(UUID groupId) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/group/" + encode(groupId.toString())
                    + "/members");
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
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/group/" + encode(group.getId().toString())
                    + "/members/" + encode(userId.toString()));

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
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/group/" + encode(group.getId().toString())
                    + "/members/" + encode(userId.toString()) + "/role");

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
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/" + encode(emailAddress) + "/groups");
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

    public void provisionDevice(String deviceId) {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("deviceId", deviceId);
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/device");
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

    public void deprovisionDevice(String deviceId) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/device/" + encode(deviceId));
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

    @SuppressWarnings("unchecked")
    public Map<String, String> getUserDeviceMetadata(String deviceId) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/device/" + encode(deviceId));
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(endpoint)
                            .build();
            SidecarResponse response = sidecarGetRequest.send();

            if (response.getStatusCode() == 200) {
                return Collections.checkedMap(mapper.readValue(response.getBody(), Map.class),
                        String.class, String.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }

    public void updateUserDeviceMetadata(String deviceId, Map<String, String> metaData) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/device/" + encode(deviceId));
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

    @SuppressWarnings("unchecked")
    public List<Device> getDevicesForUser() {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/devices");
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
    public void addNotificationToken(PlatformDeviceToken token) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/notifications/token");
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

    public UUID addNotificationRule(String name, String description, String stream, String key, Double min, Double max) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("stream", stream);
        payload.put("key", key);
        payload.put("min", min);
        payload.put("max", max);

        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/notifications/rule");
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

    public UUID updateNotificationRule(UUID ruleId, String name, String description, String stream, String key, Double min, Double max) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("stream", stream);
        payload.put("key", key);
        payload.put("min", min);
        payload.put("max", max);

        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/notifications/rule/" +
                    encode(ruleId.toString()));
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

    public NotificationRule getNotificationRule(UUID ruleId) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/notifications/rule/" +
                    encode(ruleId.toString()));
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

    @SuppressWarnings("unchecked")
    public List<NotificationRule> getNotificationRules() {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/notifications/rules");
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

    public void deleteNotificationRule(UUID ruleId) {
        try {
            URL endpoint = clientConfig.fullUrlForPath("/rest/v1/provision/user/notifications/rule/" +
                    encode(ruleId.toString()));
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
    public List<UserAnswerBucket> postUserQuery(String type, Query query) {
        String path = "/rest/v1/query/user/devices/" + encode(type);
        return postUserBasedQuery(path, query);
    }

    public List<UserAnswerBucket> postUserDeviceQuery(String type, String deviceId, Query query) {
        String path = "/rest/v1/query/user/device/" + encode(deviceId) + "/" + encode(type);
        return postUserBasedQuery(path, query);
    }

    @SuppressWarnings("unchecked")
    private List<UserAnswerBucket> postUserBasedQuery(String path, Query query) {
        try {
            URL endpoint = clientConfig.fullUrlForPath(path);
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

    private String encode(String input) {
        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
