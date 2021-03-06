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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class QueryModule extends SimpleModule {

    public QueryModule() {
        super("Query Module", new Version(1, 0, 0, "SNAPSHOT", "io.sidecar", "model"));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Query.class, QueryMixin.class);
        context.setMixInAnnotations(Arg.class, ArgMixin.class);
        context.setMixInAnnotations(DataPoint.class, DataPointMixin.class);
        context.setMixInAnnotations(StatsAnswer.class,
                StatsAnswerMixin.class);
        context.setMixInAnnotations(HistogramAnswer.class,
                HistogramAnswerMixin.class);
        context.setMixInAnnotations(UserAnswerBucketMixin.class, UserAnswerBucket.class);
    }
}
