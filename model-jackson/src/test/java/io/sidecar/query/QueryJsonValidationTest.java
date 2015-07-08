package io.sidecar.query;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

public class QueryJsonValidationTest {

    private String validQueryAsJsonString;
    private ObjectNode queryAsObjectNode;
    private ModelMapper mapper = ModelMapper.instance();

    static Arg argFromJsonNode(JsonNode n) {
        return new Arg(n.path("key").asText(), n.path("value").asText());
    }

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        validQueryAsJsonString =
                IOUtils.toString(this.getClass().getResource("/proposed_query.json"));
        queryAsObjectNode = (ObjectNode) mapper.readTree(validQueryAsJsonString);
    }

    @Test(description = "Assert that a Query json object can be created when the following mandatory fields are valid")
    public void eventIsCreatedWithAllMandatoryFields() throws Exception {
        Query query = mapper.readValue(validQueryAsJsonString, Query.class);

        assertEquals(query.getStream(), queryAsObjectNode.path("stream").asText());

        List<Arg> args = Lists.newArrayList(Iterables.transform(queryAsObjectNode.path("args"),
                new Function<JsonNode, Arg>() {
                    @Override
                    public Arg apply(JsonNode input) {
                        return argFromJsonNode(input);
                    }
                }));
        assertEquals(query.getArgs(), args);
    }

    @Test(description = "Assert that a Query can't be created with a missing stream",
            expectedExceptions = JsonMappingException.class)
    public void streamIsMissing() throws Exception {
        queryAsObjectNode.remove("stream");
        mapper.readValue(queryAsObjectNode.traverse(), Query.class);
    }

    @Test(description = "Assert that a Query can't be created with a blank stream",
            expectedExceptions = JsonMappingException.class)
    public void streamIsBlank() throws Exception {
        queryAsObjectNode.put("stream", "   ");
        mapper.readValue(queryAsObjectNode.traverse(), Query.class);
    }

    @Test(description = "Assert that we can serialize and deserialize and get an equals, but not same, object")
    public void toJsonAndBackAndStillEquals() throws Exception {
        Query original = mapper.readValue(queryAsObjectNode.traverse(), Query.class);
        String serialized = mapper.writeValueAsString(original);
        Query deserialized = mapper.readValue(serialized, Query.class);

        assertEquals(deserialized, original);
        assertNotSame(deserialized, original);
    }
}
