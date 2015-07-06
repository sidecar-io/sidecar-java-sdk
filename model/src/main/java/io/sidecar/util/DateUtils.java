package io.sidecar.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;

public class DateUtils {
    public static DateTime nowUtc() {
        return new DateTime(DateTimeZone.UTC);
    }

    private DateUtils() {

    }
}
