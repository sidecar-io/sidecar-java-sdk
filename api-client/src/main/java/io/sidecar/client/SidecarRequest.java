package io.sidecar.client;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.sidecar.security.AuthorizationParmeterConstants;
import io.sidecar.security.SecurityUtils;
import io.sidecar.security.signature.Signature;
import io.sidecar.security.signature.SignatureVersion;
import io.sidecar.security.signature.SignatureVersionOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class SidecarRequest {
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final Logger LOGGER = LoggerFactory.getLogger(SidecarRequest.class);
    private static final OkHttpClient httpClient = new OkHttpClient();

    protected String accessKey;
    protected String secret;
    protected String password;
    protected String requestDate;
    protected String signatureVersion;

    abstract SidecarResponse send() throws Exception;

    protected SidecarResponse send(Request httpReq) {
        try {
            Response resp = httpClient.newCall(httpReq).execute();
            return new SidecarResponse(resp.code(), resp.body().string());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void signHeaders(String httpMethod, String URI, Request.Builder request) {
        signHeaders(httpMethod, URI, request, null);
    }

    protected void signHeaders(String httpMethod, String uri, Request.Builder request, String payload) {
        String md5 = null;

        LOGGER.debug("Beging to sign the payload");
        if (payload != null) {
            LOGGER.debug("Payload is not null, add the optional md5 hash header value.");
            md5 = SecurityUtils.md5(payload);
            request.addHeader(AuthorizationParmeterConstants.SC_API_MD5_HEADER, md5);
            LOGGER.debug(AuthorizationParmeterConstants.SC_API_MD5_HEADER + " = " + md5);
        }

        // Version one support only right now
        SignatureVersion version = new SignatureVersionOne.Builder()
                .withDate(requestDate)
                .withHttpMethod(httpMethod)
                .withURI(uri)
                .withContentMd5(md5)
                .build();

        LOGGER.debug("Parameters used to sign string: ");
        LOGGER.debug("   date " + requestDate);
        LOGGER.debug("   httpMethod " + httpMethod);
        LOGGER.debug("   uri " + uri);
        LOGGER.debug("   contentMd5 " + md5);
        LOGGER.debug("   signature version " + signatureVersion);

        LOGGER.debug("Begin adding the required header values");
        // add required headers
        request.addHeader("Content-Type", "application/json");
        LOGGER.debug("Content-Type = " + "application/json");
        // date
        request.addHeader(AuthorizationParmeterConstants.SC_API_DATE_HEADER, requestDate);
        LOGGER.debug(AuthorizationParmeterConstants.SC_API_DATE_HEADER + " = " + requestDate);

        if (signatureVersion != null) {
            try {
                LOGGER.debug("Signature Version is: " + signatureVersion);

                // sign the request
                LOGGER.debug("Add the Signature headers....");
                request.addHeader(AuthorizationParmeterConstants.SC_API_SIG_VERSION_HEADER,
                        signatureVersion);
                LOGGER.debug(AuthorizationParmeterConstants.SC_API_SIG_VERSION_HEADER + " = " + signatureVersion);

                String sig = Signature.calculateRFC2104HMAC(version.getStringToSign(), secret);
                LOGGER.debug("Signed Signature Hash is " + sig);
                request.addHeader(AuthorizationParmeterConstants.SC_API_ACCESSKEY_HEADER,
                        AuthorizationParmeterConstants.SC_API_ACCESSKEY_PREFIX + " " + accessKey
                                + ":"
                                + sig);
                LOGGER.debug(AuthorizationParmeterConstants.SC_API_ACCESSKEY_HEADER + " = " +
                                AuthorizationParmeterConstants.SC_API_ACCESSKEY_PREFIX + " " + accessKey
                                + ":"
                                + sig
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // this is simple auth using the credential
            request.addHeader(AuthorizationParmeterConstants.SC_API_ACCESSKEY_HEADER,
                    AuthorizationParmeterConstants.SC_API_ACCESSKEY_PREFIX + " " + accessKey
                            + ":"
                            + password);

        }

    }


}
