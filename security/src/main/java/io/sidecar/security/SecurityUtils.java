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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Throwables;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

public class SecurityUtils {

    private static final String MD5_ALGORITHM = "MD5";

    /**
     * Performs base64-encoding of input bytes.
     *
     * @param rawData * Array of bytes to be encoded.
     * @return * The base64 encoded string representation of rawData.
     */
    public static String encodeBase64(byte[] rawData) {
        try {
            return new String(Base64.encodeBase64(rawData), "UTF-8");
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
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
            return String.copyValueOf(Hex.encodeHex(digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
