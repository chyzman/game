package com.chyzman.dominion;

import com.chyzman.util.Id;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Entity;
import dev.dominion.ecs.api.Results;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Framework {

    private final Id name;
    protected final Map<Class<?>, Supplier<?>> componentConstructors;

    protected Framework(Id name, Map<Class<?>, Supplier<?>> componentConstructors) {
        this.name = name;
        this.componentConstructors = componentConstructors;
    }

    public Frame framed(Id name) {
        return new Frame(name).fromFramework(this);
    }

    public void addTo(Dominion dominion) {
        var array = Stream.concat(Stream.of(this.name), this.componentConstructors.values().stream().map(Supplier::get))
                .toArray();

        dominion.createEntity(array);
    }

    public void addToWith(Dominion dominion, Object... components) {
        var mainMap = new HashMap<>();

        mainMap.put(Id.class, this.name);

        for (var component : components) mainMap.put(component.getClass(), component);

        this.componentConstructors.forEach((aClass, supplier) -> {
            if(!mainMap.containsKey(aClass)) mainMap.put(aClass, supplier.get());
        });

        dominion.createEntity(mainMap.values().toArray());
    }

    public Results<? extends Entity> find(FramedDominion dominion) {
        var stream = Stream.concat(this.componentConstructors.keySet().stream(), Stream.of((Class<?>) Id.class));

        return dominion.findEntitiesWith(stream.toArray(Class[]::new));
    }

    public Results<? extends Entity> findWith(FramedDominion dominion, Class<?>... components) {
        var stream = Stream.concat(Stream.concat(this.componentConstructors.keySet().stream(), Stream.of((Class<?>) Id.class)), Stream.of(components));

        return dominion.findEntitiesWith(stream.toArray(Class[]::new));
    }
}
