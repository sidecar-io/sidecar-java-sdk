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

import java.util.Objects;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

public final class Arg {

    private final String key;
    private final String value;

    public Arg(String key, String value) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(key));
        this.key = key;

        Preconditions.checkArgument(StringUtils.isNotBlank(value));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(value));
        this.value = value;
    }

    /**
     * The query argument name, ie "limit".
     *
     * @return  the name of the argument
     */
    public String getKey() {
        return key;
    }

    /**
     * The value associated with the named key argument, ie "5"
     * @return the value of the argument
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Arg{" +
               "key=" + key +
               ", value=" + value +
               "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Arg that = (Arg) o;

        return Objects.equals(this.key, that.key) &&
               Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
