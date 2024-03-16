package com.chyzman.systems;

import dev.dominion.ecs.api.Dominion;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface DomSystem extends Runnable {

    Dominion dominion();

    String name();

    static DomSystem create(Dominion dominion, String name, Consumer<Dominion> consumer){
        return new DomSystem() {
            @Override
            public Dominion dominion() {
                return dominion;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public void run() {
                consumer.accept(dominion());
            }
        };
    }

    static DomSystem create(Dominion dominion, String name, Supplier<Double> deltaTime, BiConsumer<Dominion, Double> consumer){
        return new DomSystem() {
            @Override
            public Dominion dominion() {
                return dominion;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public void run() {
                consumer.accept(dominion(), deltaTime.get());
            }
        };
    }
}
