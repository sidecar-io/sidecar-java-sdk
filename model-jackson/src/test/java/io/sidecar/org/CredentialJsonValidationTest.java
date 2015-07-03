package io.sidecar.org;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.sidecar.credential.Credential;
import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;


/**
 * Test methods that assert that an Event object can only be created from a true valid json
 * payload.
 */
public class CredentialJsonValidationTest {

    private String validProvisionAsJson;
    private ObjectNode credentialAsObjectNode;
    private ModelMapper mapper = new ModelMapper();

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        validProvisionAsJson =
              IOUtils.toString(this.getClass().getResource("/credential_access_payload.json"));
        credentialAsObjectNode = (ObjectNode) mapper.readTree(validProvisionAsJson);
    }


    @Test(description = "Assert that an Credential access json object can be created when the following mandatory "
            + "fields are valid")
    public void accessIsCreatedWithAllMandatoryFields() throws Exception {
        Credential credential = mapper.readValue(validProvisionAsJson, Credential.class);

        assertEquals(credential.getUsername(), credentialAsObjectNode.path("username").asText());
        assertEquals(credential.getPassword(), credentialAsObjectNode.path("password").asText());
    }


    @Test(description = "Assert that  Credential access object can't be created with a missing username",
          expectedExceptions = JsonMappingException.class)
    public void usernameIsMissing() throws Exception {
        credentialAsObjectNode.remove("username");
        mapper.readValue(credentialAsObjectNode.traverse(), Credential.class);
    }

    @Test(description = "Assert that  Credential access object can't be created with a blank username",
          expectedExceptions = JsonMappingException.class)
    public void usernameIsBlank() throws Exception {
        credentialAsObjectNode.put("username", "   ");
        mapper.readValue(credentialAsObjectNode.traverse(), Credential.class);
    }

    @Test(description = "Assert that Credential access object can't be created with a missing password",
          expectedExceptions = JsonMappingException.class)
    public void passwordIsMissing() throws Exception {
        credentialAsObjectNode.remove("password");
        mapper.readValue(credentialAsObjectNode.traverse(), Credential.class);
    }

    @Test(description = "Assert that  Credential access object can't be created with a blank password",
          expectedExceptions = JsonMappingException.class)
    public void passwordIsBlank() throws Exception {
        credentialAsObjectNode.put("password", "   ");
        mapper.readValue(credentialAsObjectNode.traverse(), Credential.class);
    }


    @Test(description = "Assert that we can serialize and deserialize and get an equals, but not same, object")
    public void toJsonAndBackAndStillEquals() throws Exception {
        Credential original = mapper.readValue(credentialAsObjectNode.traverse(), Credential.class);
        String serialized = mapper.writeValueAsString(original);
        Credential deserialized = mapper.readValue(serialized, Credential.class);

        assertEquals(deserialized, original);
        assertNotSame(deserialized, original);
    }

}
