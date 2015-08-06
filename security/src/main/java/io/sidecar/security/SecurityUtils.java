package io.sidecar.security;


import static com.google.common.base.Preconditions.checkNotNull;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
     *
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
            return Hex.encodeHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * AES encrypt
     */
    @SuppressWarnings("unused")
    public static String encrypt(String plainText, String key)
            throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encryptedByte);
    }

    /**
     * AES decrypt
     */
    @SuppressWarnings("unused")
    public static String decrypt(String encryptedText, String key)
            throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        return new String(decryptedByte);
    }
}

