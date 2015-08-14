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
        context.setMixInAnnotations(StatsHistogramAnswer.class,
                StatsHistogramAnswerMixin.class);
        context.setMixInAnnotations(UserAnswerBucketMixin.class, UserAnswerBucket.class);
    }
}
