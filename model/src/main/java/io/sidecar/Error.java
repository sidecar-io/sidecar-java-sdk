package io.sidecar;

import com.google.common.base.Preconditions;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;

public final class Error {

    public final static String DEFAULT_STATUS = "error";
    private final String status;
    private final String message;

    public Error(String message) {
        this(DEFAULT_STATUS, message);
    }

    public Error(String status, String message) {
        Preconditions.checkArgument(StringUtils.isNotBlank(message));
        this.status = status;

        Preconditions.checkArgument(StringUtils.isNotBlank(message));
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "status=" + status +
                ", message=" + message +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Error that = (Error) o;

        return Objects.equals(this.status, that.status) &&
                Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }
}
