package io.sidecar.client;

public class SidecarClientException extends RuntimeException {

    private final int statusCode;
    private final String message;

    SidecarClientException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @SuppressWarnings("unused")
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
