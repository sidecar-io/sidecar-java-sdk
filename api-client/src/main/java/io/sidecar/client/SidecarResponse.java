package io.sidecar.client;

import static com.google.common.base.Preconditions.checkArgument;

public class SidecarResponse {

    private final int statusCode;
    private final String body;

    SidecarResponse(int statusCode, String body) {
        checkArgument(statusCode >= 100 && statusCode < 600);
        this.statusCode = statusCode;

        this.body = body == null ? "" : body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "SidecarResponse{" +
                "statusCode=" + statusCode +
                ", body='" + body + '\'' +
                '}';
    }
}
