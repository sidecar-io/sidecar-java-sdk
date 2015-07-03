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
