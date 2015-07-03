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
