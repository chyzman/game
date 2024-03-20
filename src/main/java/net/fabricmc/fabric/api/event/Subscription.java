package net.fabricmc.fabric.api.event;

import com.chyzman.util.Id;

public class Subscription<T> {
    private final Event<T> event;

    protected final Id phase;
    protected final T subscriber;

    public Subscription(Event<T> event, Id phase, T subscriber) {
        this.event = event;
        this.phase = phase;
        this.subscriber = subscriber;
    }

    public Event<T> cancel() {
        return event.unregister(this.phase, this.subscriber);
    }
}
