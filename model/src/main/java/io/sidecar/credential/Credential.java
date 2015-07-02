package io.sidecar.credential;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;

public final class Credential {

    private final String username;
    private final String password;


    public Credential(String username, String password) {

        Preconditions.checkNotNull(username);
        Preconditions.checkArgument(StringUtils.isNotBlank(username));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(username));
        this.username = username;

        Preconditions.checkNotNull(password);
        Preconditions.checkArgument(StringUtils.isNotBlank(password));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(password));
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public String toString() {
        return "Credential{" +
               "username=" + username +
               ", password=" + password +
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

        Credential that = (Credential) o;

        return
              Objects.equals(this.username, that.username) &&
              Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
