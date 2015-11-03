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

package io.sidecar.event;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.Set;

public class KeyTag {

    private final String key;
    private final Set<String> tags;

    /**
     * Optional tags that can be added to named reading keys.
     *
     * @param key  - The associated key name in the reading
     * @param tags - A list of tags to apply to
     */
    public KeyTag(String key, Set<String> tags) {
        this.key = checkNotNull(key, "key must be present for a keytag");
        this.tags = checkNotNull(tags, "tags must be present for a keytag");
    }

    /**
     * @return - The name of the key that is being tagged
     */
    public String getKey() {
        return key;
    }

    /**
     * @return - The list of tags for the named reading key
     */
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyTag keyTag = (KeyTag) o;
        return Objects.equals(key, keyTag.key) &&
                Objects.equals(tags, keyTag.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, tags);
    }

    @Override
    public String toString() {
        return "KeyTag{" +
                "key='" + key + '\'' +
                ", tags=" + tags +
                '}';
    }
}
