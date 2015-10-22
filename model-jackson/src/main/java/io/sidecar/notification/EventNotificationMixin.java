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

package io.sidecar.notification;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

@SuppressWarnings("unused") //Used by Jackson
abstract class EventNotificationMixin {

    private EventNotificationMixin(@JsonProperty("rule") NotificationRule rule,
                                   @JsonProperty("orgId") UUID orgId,
                                   @JsonProperty("appId") UUID appId,
                                   @JsonProperty("userId") UUID userId,
                                   @JsonProperty("ts") DateTime timestamp) {}

    @JsonGetter("ts")
    abstract DateTime getTimestamp();
}
