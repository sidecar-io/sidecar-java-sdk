package io.sidecar.org;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract public class MetaDataMixin {

    private MetaDataMixin(@JsonProperty("data") Map data) {
    }

    @JsonGetter("data")
    abstract Map getData();

}
