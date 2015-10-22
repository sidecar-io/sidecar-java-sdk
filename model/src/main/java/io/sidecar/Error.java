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
