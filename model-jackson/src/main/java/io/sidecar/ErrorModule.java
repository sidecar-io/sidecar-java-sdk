package io.sidecar;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ErrorModule extends SimpleModule {

    public ErrorModule() {
        super("Error Module", new Version(1, 0, 0, "SNAPSHOT", "io.sidecar", "model"));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Error.class, ErrorMixin.class);
    }
}
