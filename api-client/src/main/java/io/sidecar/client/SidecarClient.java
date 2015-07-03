package io.sidecar.client;

import static com.google.common.base.Throwables.propagate;
import static io.sidecar.security.signature.SignatureVersion.Version.ONE;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import io.sidecar.access.AccessKey;
import io.sidecar.credential.Credential;
import io.sidecar.event.Event;
import io.sidecar.jackson.ModelMapper;
import io.sidecar.notification.NotificationRule;
import io.sidecar.org.PlatformDeviceToken;
import io.sidecar.org.UserGroup;
import io.sidecar.org.UserGroupMember;
import io.sidecar.query.Query;
import io.sidecar.query.UserAnswerBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @SuppressWarnings("unused")
    public AccessKey postAuthUser(Credential credential) {
        String path = "/rest/v1/provision/application/auth";
        URL baseUrl = clientConfig.getRestApiBasePath();

        try {
            SidecarPostRequest sidecarPostRequest = new SidecarPostRequest.Builder(
                    accessKey.getKeyId(), "", accessKey.getSecret())
                    .withSignatureVersion(ONE)
                    .withUrl(new URL(baseUrl.toString() + path))
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

    @SuppressWarnings("unused")
    public List<UserAnswerBucket> postUserQuery(String type, Query query) {
        String path = "/rest/v1/query/user/devices/" + type;
        return _postUserBasedQuery(path, query);
    }

    public List<UserAnswerBucket> postUserDeviceQuery(String type, UUID deviceId, Query query) {
        String path = "/rest/v1/query/user/device/" + deviceId.toString() + "/" + type;
        return _postUserBasedQuery(path, query);
    }

    @SuppressWarnings("unchecked")
    private List<UserAnswerBucket> _postUserBasedQuery(String path, Query query) {
        URL baseUrl = clientConfig.getRestApiBasePath();

        try {
            SidecarPostRequest sidecarPostRequest = new SidecarPostRequest.Builder(
                    accessKey.getKeyId(), "", accessKey.getSecret())
                    .withSignatureVersion(ONE)
                    .withUrl(new URL(baseUrl.toString() + path))
                    .withPayload(query)
                    .build();
            SidecarResponse response = sidecarPostRequest.send();

            if (response.getStatusCode() == 200) {
                LOGGER.debug(response.getBody());
                return Collections.checkedList(mapper.readValue(response.getBody(), List.class),UserAnswerBucket.class);
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }
    }


    public UUID postEvent(Event event) {
        String path = "/rest/v1/event";
        URL baseUrl = clientConfig.getRestApiBasePath();

        try {
            SidecarPostRequest sidecarPostRequest = new SidecarPostRequest.Builder(
                    accessKey.getKeyId(), "", accessKey.getSecret())
                    .withSignatureVersion(ONE)
                    .withUrl(new URL(baseUrl.toString() + path))
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

    @SuppressWarnings("unused")
    public UserGroup createNewUserGroup(String newGroupName) {
        try {
            HashMap<String, String> payload = new HashMap<>();
            payload.put("name", newGroupName);

            String path = "/rest/v1/provision/group";

            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarPostRequest
                    sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
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
    public UUID userIdForUserAddress(String emailAddress) {
        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            String path = "/rest/v1/provision/user/" + emailAddress;

            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
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

    @SuppressWarnings("unused")
    public UserGroup addUserToUserGroup(UserGroup userGroup, UserGroupMember member) {
        String userGroupId = userGroup.getId().toString();
        String path = "/rest/v1/provision/group/" + userGroupId + "/members";
        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
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

    @SuppressWarnings({"unchecked","unused"})
    public List<UUID> getGroupsForUser(String emailAddress) {
        String path = "/rest/v1/provision/user/" + emailAddress + "/groups";
        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarGetRequest sidecarGetRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
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

    @SuppressWarnings("unused")
    public void provisionDevice(UUID deviceId) {
        String path = "/rest/v1/provision/user/device";
        Map<String, String> metaData = new HashMap<>();
        metaData.put("deviceId", deviceId.toString());
        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
                            .withPayload(metaData)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            //The response body is empty if 200, so only check if we do not have a 200 response
            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }

    }

    @SuppressWarnings("unused")
    public AccessKey createNewUser(String emailAddress, String password) {
        try {
            String path = "/rest/v1/provision/application/user";
            Credential credential = new Credential(emailAddress, password);

            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarPostRequest
                    sidecarRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withUrl(new URL(baseUrl.toString() + path))
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
    public void provisionDeviceToken(String platform, String deviceToken) {
        String path = "/rest/v1/provision/user/notifications/token";
        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
                            .withPayload(new PlatformDeviceToken.Builder()
                                    .deviceToken(deviceToken)
                                    .platform(platform).build())
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            // check for an accepted response code
            if (response.getStatusCode() != 204) {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }

    }

    @SuppressWarnings("unused")
    public UUID addNotificationRule(String name, String description, String stream, String key, Double min, Double max) {
        String path = "/rest/v1/provision/user/notifications/rule";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("stream", stream);
        payload.put("key", key);
        payload.put("min", min);
        payload.put("max", max);

        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarPostRequest sidecarPostRequest =
                    new SidecarPostRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
                            .withPayload(payload)
                            .build();
            SidecarResponse response = sidecarPostRequest.send();

            // check for an OK response code
            if (response.getStatusCode() == 200) {
                return UUID
                        .fromString(mapper.readTree(response.getBody()).get("id").textValue());
            } else {
                throw new SidecarClientException(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            throw propagate(e);
        }

    }

    @SuppressWarnings("unused")
    public NotificationRule getNotificationRule(UUID ruleId) {
        String path = "/rest/v1/provision/user/notifications/rule/" + ruleId.toString();

        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarGetRequest sidecarPostRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
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

    @SuppressWarnings({"unused","unchecked"})
    public List<NotificationRule> getNotificationRules(UUID appId, UUID userId) {
        String path = "/rest/v1/provision/user/notifications/rules";

        try {
            URL baseUrl = clientConfig.getRestApiBasePath();
            SidecarGetRequest sidecarPostRequest =
                    new SidecarGetRequest.Builder(accessKey.getKeyId(), "", accessKey.getSecret())
                            .withSignatureVersion(ONE)
                            .withUrl(new URL(baseUrl.toString() + path))
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

}
