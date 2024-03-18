package com.chyzman.registry;

import com.chyzman.util.Id;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Frame {

    private final Id name;
    private final Map<Class<?>, Supplier<?>> componentConstructors = new HashMap<>();

    public Frame(Id name){
        this.name = name;
    }

    public Frame fromFramework(Framework framework) {
        this.componentConstructors.putAll(framework.componentConstructors);

        return this;
    }

    public <T> Frame addComponent(Class<T> clazz, Supplier<T> supplier) {
        this.componentConstructors.put(clazz, supplier);

        return this;
    }

    public Frame removeComponent(Class<?> clazz) {
        this.componentConstructors.remove(clazz);

        return this;
    }

    public Framework build() {
        return new Framework(name, Map.copyOf(componentConstructors));
    }
}
