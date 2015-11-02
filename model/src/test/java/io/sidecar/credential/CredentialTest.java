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

package io.sidecar.credential;

import org.testng.annotations.Test;

public class CredentialTest {


    @Test(description = "Assert than a username can't contain new lines",
          expectedExceptions = IllegalArgumentException.class)
    public void usernameCantContainLineBreaks() {
        new Credential("someuser\n", "somepassword");
    }

    @Test(description = "Assert than a username can't contain tabs",
          expectedExceptions = IllegalArgumentException.class)
    public void usernameCantContainTabs() {
        new Credential("some\tuser", "somepassword");
    }

    @Test(description = "Assert than a username can't contain spaces",
          expectedExceptions = IllegalArgumentException.class)
    public void usernameCantContainSpaces() {
        new Credential("some user", "somepassword");
    }

    @Test(description = "Assert than a password can't contain new lines",
          expectedExceptions = IllegalArgumentException.class)
    public void passwordCantContainLineBreaks() {
        new Credential("someuser", "somepassword\n");
    }

    @Test(description = "Assert than a password can't contain tabs",
          expectedExceptions = IllegalArgumentException.class)
    public void passwordCantContainTabs() {
        new Credential("someuser", "somepa\tssword");
    }

    @Test(description = "Assert than a password can't contain spaces",
          expectedExceptions = IllegalArgumentException.class)
    public void passwordCantContainSpaces() {
        new Credential("someuser", "some password");
    }


}
