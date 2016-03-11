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

package io.sidecar.query;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

public class UserAnswerBucket<T> {

    private final UUID userId;
    private final String deviceId;
    private final T answer;

    public UserAnswerBucket(UUID userId, String deviceId, T answer) {
        checkNotNull(answer);
        checkArgument((userId != null || deviceId != null), "One id must be non-null when generating an AnswerBucket");

        this.deviceId = deviceId;
        this.userId = userId;
        this.answer = answer;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public T getAnswer() {
        return answer;
    }

}
