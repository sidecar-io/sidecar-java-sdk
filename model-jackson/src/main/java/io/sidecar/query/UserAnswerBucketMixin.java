package io.sidecar.query;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@SuppressWarnings("unused") //Used by Jackson
abstract public class UserAnswerBucketMixin {

    private UserAnswerBucketMixin(@JsonProperty("userId") UUID userId,
                                  @JsonProperty("deviceId") UUID deviceId,
                                  @JsonProperty("answer") Answer answer) {
    }
}
