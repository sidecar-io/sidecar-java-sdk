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

package io.sidecar.query;

import java.util.List;
import java.util.Objects;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.sidecar.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;

// TODO: make this a builder..
public final class Query {

    private final String stream;
    private final ImmutableList<Arg> args;

    public Query(String stream, List<Arg> args) {

        Preconditions.checkArgument(StringUtils.isNotBlank(stream));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(stream));
        this.stream = stream;

        this.args = CollectionUtils.filterNulls(args);
    }

    /**
     * The Stream the query should be executed against. This should correspond to the stream name the event
     * was ingested into.
     *
     * @return The name of the stream.
     */
    public String getStream() {
        return stream;
    }

    public ImmutableList<Arg> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "Query{" +
                "stream=" + stream +
                ", args=" + args +
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

        Query that = (Query) o;

        return Objects.equals(this.stream, that.stream) &&
                Objects.equals(this.args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stream, args);
    }
}
