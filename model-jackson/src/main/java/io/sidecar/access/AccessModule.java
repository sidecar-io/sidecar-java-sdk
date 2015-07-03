package io.sidecar.access;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;


public class AccessModule extends SimpleModule {

    public AccessModule() {
        super("Access Module", new Version(1, 0, 0, "SNAPSHOT", "io.sidecar", "model"));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(AccessKey.class, AccessKeyMixin.class);
    }
}
