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

package io.sidecar.security.signature;

public abstract class SignatureVersion {

    static final String DELIMITER = "\n";

    protected abstract Version getVersion();

    public abstract String getStringToSign();

    public enum Version {
        ONE("1");

        public final String digit;

        Version(String digit) {
            this.digit = digit;
        }

        @SuppressWarnings("unused")
        public static Version fromString(String text) {
            if (text == null) {
                return null;
            }
            for (Version v : Version.values()) {
                if (text.trim().equalsIgnoreCase(v.digit)) {
                    return v;
                }
            }
            return null;
        }
    }
}
