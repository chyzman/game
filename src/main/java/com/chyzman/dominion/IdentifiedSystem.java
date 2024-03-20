package com.chyzman.dominion;

import com.chyzman.util.Id;
import dev.dominion.ecs.api.Dominion;
import io.wispforest.endec.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IdentifiedSystem<D> extends Runnable {

    Id id();

    D dominion();

    @Override
    void run();

    static <D extends Dominion> IdentifiedSystem<D> of(Id id, D dominion, Consumer<D> consumer) {
        return new IdentifiedSystem<>() {
            @Override
            public Id id() {
                return id;
            }

            @Override
            public D dominion() {
                return dominion;
            }

            @Override
            public void run() {
                consumer.accept(dominion());
            }
        };
    }

    static <D extends Dominion> IdentifiedSystem<D> of(Id id, D dominion, Supplier<Double> deltaTime, BiConsumer<D, Double> biConsumer) {
        return of(id, dominion, dom -> biConsumer.accept(dom, deltaTime.get()));
    }

    default IdentifiedSystem<D> safe(int timeoutInTicks, TriConsumer<D, Id, Exception> exceptionHandling) {
        return new IdentifiedSystem<>() {
            private int tickCount = -1;

            @Override
            public Id id() {
                return IdentifiedSystem.this.id();
            }

            @Override
            public D dominion() {
                return IdentifiedSystem.this.dominion();
            }

            @Override
            public void run() {
                if(this.tickCount != -1) {
                    this.tickCount--;

                    if(this.tickCount < timeoutInTicks) return;
                }

                try {
                    IdentifiedSystem.this.run();

                    this.tickCount = -1;
                } catch (Exception e) {
                    exceptionHandling.accept(dominion(), this.id(), e);

                    this.tickCount = 0;
                }
            }
        };
    }

    default IdentifiedSystem<D> safeTickless(TriConsumer<D, Id,  Exception> exceptionHandling) {
        return new IdentifiedSystem<>() {
            @Override
            public Id id() {
                return IdentifiedSystem.this.id();
            }

            @Override
            public D dominion() {
                return IdentifiedSystem.this.dominion();
            }

            @Override
            public void run() {
                try {
                    IdentifiedSystem.this.run();
                } catch (Exception e) {
                    exceptionHandling.accept(dominion(), this.id(), e);
                }
            }
        };
    }
}
