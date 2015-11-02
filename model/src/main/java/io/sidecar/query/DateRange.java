/*
 * Copyright 2015 QSense, Inc.
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

package io.sidecar.query;

import com.google.common.base.Preconditions;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class DateRange {

    public final DateTime begin;
    public final DateTime end;
    public final Interval duration;

    public DateRange(final DateTime begin, final DateTime end) {
        this.begin = begin;
        this.end = end;
        Preconditions.checkArgument(end.isAfter(begin));

        this.duration = new Interval(this.begin, this.end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DateRange dateRange = (DateRange) o;

        if (!begin.equals(dateRange.begin)) {
            return false;
        }
        if (!duration.equals(dateRange.duration)) {
            return false;
        }
        if (!end.equals(dateRange.end)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DateRange {" +
               "begin=" + begin +
               ", end=" + end +
               "} " + super.toString();
    }
}