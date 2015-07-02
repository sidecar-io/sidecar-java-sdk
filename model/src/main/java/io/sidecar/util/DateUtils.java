package io.sidecar.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;

public class DateUtils {

    public static DateTime nowUtc() {
        return new DateTime(DateTimeZone.UTC);
    }

    /**
     * Test if a date is older than a certain number of minutes.
     *
     * @param requestDateTimeAsString - the date to check against
     * @param minutesAgo              - the number of minutes to check against
     * @return - true if the request date is older than the number of minutes passed in
     */
    @SuppressWarnings("unused")
    public static boolean isExpiredRequestDate(String requestDateTimeAsString, int minutesAgo)
            throws ParseException {

        if (StringUtils.isBlank(requestDateTimeAsString)) {
            return true;
        }

        DateTime requestDateTime = new DateTime(requestDateTimeAsString, DateTimeZone.UTC);
        DateTime timeOutThreshold = nowUtc().minusMinutes(minutesAgo);

        // if the header date is older than this, fail
        return requestDateTime.isBefore(timeOutThreshold);
    }
}
