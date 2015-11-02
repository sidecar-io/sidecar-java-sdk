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

package io.sidecar;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import io.sidecar.query.Arg;
import org.apache.commons.lang.StringUtils;


public class ModelUtils {

    public static boolean isValidStreamId(String stream) {
        return StringUtils.isNotBlank(stream) && CharMatcher.WHITESPACE.matchesNoneOf(stream);
    }

    public static boolean isValidReadingKey(String key) {
        return StringUtils.isNotBlank(key) && key.length() <= 40 && CharMatcher.WHITESPACE.matchesNoneOf(key);
    }

    public static String getArgKeyValue(String key, ImmutableList<Arg> args) {
        for (Arg a : args) {
            if (a.getKey().equalsIgnoreCase(key)) {
                return a.getValue();
            }
        }
        return null;
    }

}
