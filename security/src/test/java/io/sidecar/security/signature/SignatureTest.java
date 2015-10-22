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

package io.sidecar.security.signature;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.SignatureException;

public class SignatureTest {

    @Test(description = "test signature is created")
    public void testSignatureIsCreated() throws Exception {
        String sig = Signature.calculateRFC2104HMAC("foo", "bar");
        Assert.assertNotNull(sig);
    }

    @Test(description = "test signature key is valid", expectedExceptions = SignatureException.class)
    public void testSignatureKeyIsValid() throws Exception {
        String sig = Signature.calculateRFC2104HMAC("foo", null);
        Assert.assertNotNull(sig);
    }

    @Test(description = "test signature data is valid", expectedExceptions = SignatureException.class)
    public void testSignatureDataIsValid() throws Exception {
        String sig = Signature.calculateRFC2104HMAC(null, "key");
        Assert.assertNotNull(sig);
    }

}
