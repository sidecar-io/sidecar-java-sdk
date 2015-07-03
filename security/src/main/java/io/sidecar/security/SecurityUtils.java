package io.sidecar.security;


import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

public class SecurityUtils {

    private static final String MD5_ALGORITHM = "MD5";

    /**
     * Performs base64-encoding of input bytes.
     *
     * @param rawData * Array of bytes to be encoded.
     * @return * The base64 encoded string representation of rawData.
     */
    public static String encodeBase64(byte[] rawData) {
        return DatatypeConverter.printBase64Binary(rawData);
    }

    /**
     * Generates a String representation of an MD5 Checksum from a given String input.
     * @param input - A non-null String.
     * @return an MD5 checksum as a Hex encoded String.
     */
    public static String md5(String input) {
        checkNotNull(input);
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance(MD5_ALGORITHM);

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
