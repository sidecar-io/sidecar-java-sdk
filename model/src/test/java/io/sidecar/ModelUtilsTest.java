package io.sidecar;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;

import static io.sidecar.ModelUtils.isValidDeviceId;
import static org.testng.Assert.*;


public class ModelUtilsTest {

    @Test
    public void testIsValidDeviceId_happyPath() throws Exception {
        assertTrue(isValidDeviceId("ABC-123-XYZ"));
    }

    @Test
    public void testIsValidDeviceId_punctuationOk() throws Exception {
        assertTrue(isValidDeviceId("ABC-!@#-{}~"));
    }

    @Test
    public void testIsValidDeviceId_tooShort() throws Exception {
        assertFalse(isValidDeviceId("1234567"));
    }

    @Test
    public void testIsValidDeviceId_tooLong() throws Exception {
        assertFalse(isValidDeviceId(RandomStringUtils.randomAlphabetic(41)));
    }

    @Test
    public void testIsValidDeviceId_null() throws Exception {
        assertFalse(isValidDeviceId(null));
    }

    @Test
    public void testIsValidDeviceId_empty() throws Exception {
        assertFalse(isValidDeviceId(""));
    }

    @Test
    public void testIsValidDeviceId_whiteSpace() throws Exception {
        assertFalse(isValidDeviceId("ABC-123 XYZ"));
    }

    @Test
    public void testIsValidDeviceId_nonPrinting() throws Exception {
        String backspaceCharacter = Character.toString((char) 9);
        assertFalse(isValidDeviceId("ABC-123-" + backspaceCharacter));
    }
}