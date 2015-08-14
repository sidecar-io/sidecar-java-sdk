package io.sidecar;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import io.sidecar.query.Arg;
import org.apache.commons.lang.StringUtils;


public class ModelUtils {

    public static boolean isValidStreamId(String stream) {
        return StringUtils.isNotBlank(stream) && CharMatcher.WHITESPACE.matchesNoneOf(stream);
    }

    public static boolean isValidReadingKey(String key) {
        return StringUtils.isNotBlank(key) && key.length() <= 40 && CharMatcher.WHITESPACE.matchesNoneOf(key);
    }

    public static String getArgKeyValue(String key, ImmutableList<Arg> args) {
        for (Arg a : args) {
            if (a.getKey().equalsIgnoreCase(key)) {
                return a.getValue();
            }
        }
        return null;
    }

}
