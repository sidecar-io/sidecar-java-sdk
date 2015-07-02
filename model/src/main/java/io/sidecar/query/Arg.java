package io.sidecar.query;

import java.util.Objects;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

public final class Arg {

    private final String key;
    private final String value;

    public Arg(String key, String value) {
        Preconditions.checkArgument(StringUtils.isNotBlank(key));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(key));
        this.key = key;

        Preconditions.checkArgument(StringUtils.isNotBlank(value));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(value));
        this.value = value;
    }

    /**
     * The query argument name, ie "limit".
     *
     * @return  the name of the argument
     */
    public String getKey() {
        return key;
    }

    /**
     * The value associated with the named key argument, ie "5"
     * @return the value of the argument
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Arg{" +
               "key=" + key +
               ", value=" + value +
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

        Arg that = (Arg) o;

        return Objects.equals(this.key, that.key) &&
               Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
