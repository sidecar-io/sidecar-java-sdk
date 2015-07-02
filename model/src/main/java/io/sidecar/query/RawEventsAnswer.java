package io.sidecar.query;

import com.google.common.collect.ImmutableList;

import io.sidecar.event.Event;


public class RawEventsAnswer implements Answer {

    private final ImmutableList<Event> events;

    private RawEventsAnswer(ImmutableList<Event> events) {
        this.events = events == null ? ImmutableList.<Event>of() : events;
    }

    public static RawEventsAnswer fromEvents(Event... events) {
        return new RawEventsAnswer(ImmutableList.copyOf(events));
    }

    public ImmutableList<Event> getEvents() {
        return events;
    }
}
