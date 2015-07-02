package io.sidecar.query;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

public class UserAnswerBucket<T> {

    private final UUID userId;
    private final UUID deviceId;
    private final T answer;

    public UserAnswerBucket(UUID userId, UUID deviceId, T answer) {
        checkNotNull(answer);
        checkArgument((userId != null || deviceId != null), "One id must be non-null when generating an AnswerBucket");

        this.deviceId = deviceId;
        this.userId = userId;
        this.answer = answer;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public T getAnswer() {
        return answer;
    }

}
