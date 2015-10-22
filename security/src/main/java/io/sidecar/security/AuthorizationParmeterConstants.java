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

package io.sidecar.security;

public final class AuthorizationParmeterConstants {

    // AWS style: header(Authorization) = SIDECAR AKIAIOSFODNN7EXAMPLE:ilyl83RwaSoYIEdixDQcA4OnAnc=
    public static final String SC_API_ACCESSKEY_PREFIX = "SIDECAR";
    public static final String SC_API_ACCESSKEY_HEADER = "Authorization";

    // Date: Tue, 27 Mar 2007 21:06:08 +0000
    public static final String SC_API_DATE_HEADER = "Date";
    public static final String SC_API_MD5_HEADER = "Content-MD5";
    public static final String SC_API_SIG_VERSION_HEADER = "Signature-Version";
    public static final int SC_API_DATE_HEADER_EXPIRATION_MINUTES = 15;

    private AuthorizationParmeterConstants() {
    }

}
