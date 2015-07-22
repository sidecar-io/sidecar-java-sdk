package io.sidecar.event;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.sidecar.geo.Location;

public class EventModule extends SimpleModule {

    public EventModule() {
        super("Event Module", new Version(1, 0, 0, "SNAPSHOT", "io.sidecar", "model"));
    }

    @Override
    public void setupModule(SetupContext context) {

        context.setMixInAnnotations(Reading.class, ReadingMixin.class);
        context.setMixInAnnotations(Location.class, LocationMixin.class);
        context.setMixInAnnotations(Event.class, EventMixin.class);
        context.setMixInAnnotations(EventWithIds.class, EventWithIdsMixin.class);
        context.setMixInAnnotations(KeyTag.class, KeyTagMixin.class);
    }

}
