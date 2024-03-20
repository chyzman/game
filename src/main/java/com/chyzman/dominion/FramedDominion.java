package com.chyzman.dominion;

import dev.dominion.ecs.api.*;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FramedDominion implements Dominion {

    private final Dominion dominion;

    public FramedDominion(Dominion dominion) {
        this.dominion = dominion;
    }

    @Override
    public String getName() {
        return this.dominion.getName();
    }

    @Override
    public Entity createEntity(Object... components) {
        return this.dominion.createEntity(components);
    }

    @Override
    public Entity createPreparedEntity(Composition.OfTypes withValues) {
        return this.dominion.createPreparedEntity(withValues);
    }

    @Override
    public Entity createEntityAs(Entity prefab, Object... components) {
        return this.dominion.createEntityAs(prefab, components);
    }

    @Override
    public boolean deleteEntity(Entity entity) {
        return this.dominion.deleteEntity(entity);
    }

    @Override
    public boolean modifyEntity(Composition.Modifier modifier) {
        return this.dominion.modifyEntity(modifier);
    }

    @Override
    public Composition composition() {
        return this.dominion.composition();
    }

    @Override
    public Scheduler createScheduler() {
        return this.dominion.createScheduler();
    }

    @Override
    public <T> Results<T> findCompositionsWith(Class<T> type) {
        return this.dominion.findCompositionsWith(type);
    }

    @Override
    public <T1, T2> ResultUtils.ResultsWith2<T1, T2> findCompositionsWith(Class<T1> type1, Class<T2> type2) {
        return ResultUtils.ResultsWith2.of(this.dominion.findCompositionsWith(type1, type2));
    }

    @Override
    public <T1, T2, T3> ResultUtils.ResultsWith3<T1, T2, T3> findCompositionsWith(Class<T1> type1, Class<T2> type2, Class<T3> type3) {
        return ResultUtils.ResultsWith3.of(this.dominion.findCompositionsWith(type1, type2, type3));
    }

    @Override
    public <T1, T2, T3, T4> ResultUtils.ResultsWith4<T1, T2, T3, T4> findCompositionsWith(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4) {
        return ResultUtils.ResultsWith4.of(this.dominion.findCompositionsWith(type1, type2, type3, type4));
    }

    @Override
    public <T1, T2, T3, T4, T5> ResultUtils.ResultsWith5<T1, T2, T3, T4, T5> findCompositionsWith(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5) {
        return ResultUtils.ResultsWith5.of(this.dominion.findCompositionsWith(type1, type2, type3, type4, type5));
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> ResultUtils.ResultsWith6<T1, T2, T3, T4, T5, T6> findCompositionsWith(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5, Class<T6> type6) {
        return ResultUtils.ResultsWith6.of(this.dominion.findCompositionsWith(type1, type2, type3, type4, type5, type6));
    }

    @Override
    public  <T extends Entity> Results<T> findAllEntities() {
        return this.dominion.findAllEntities();
    }

    public Results<? extends Entity> findEntitiesWith(Class<?> ...types) {
        if(types.length == 0) return findAllEntities();

        ResultUtils.ResultsWith1<?> results = findEntitiesWith(types[0]);

        return new Results<>() {
            @Override
            public Iterator<Entity> iterator() {
                return ResultUtils.unpackingIterable(results, With1::entity).iterator();
            }

            @Override
            public Stream<Entity> stream() {
                return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
            }

            @Override
            public Stream<Entity> parallelStream() {
                return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), true);
            }

            @Override
            public Results<Entity> without(Class<?>... componentTypes) {
                results.without(componentTypes);

                return this;
            }

            @Override
            public Results<Entity> withAlso(Class<?>... componentTypes) {
                results.withAlso(componentTypes);

                return this;
            }

            @Override
            public <S extends Enum<S>> Results<Entity> withState(S state) {
                results.withState(state);

                return this;
            }
        };
    }

    @Override
    public <T> ResultUtils.ResultsWith1<T> findEntitiesWith(Class<T> type) {
        return ResultUtils.ResultsWith1.of(this.dominion.findEntitiesWith(type));
    }

    @Override
    public <T1, T2> ResultUtils.ResultsWith2<T1, T2> findEntitiesWith(Class<T1> type1, Class<T2> type2) {
        return ResultUtils.ResultsWith2.of(this.dominion.findEntitiesWith(type1, type2));
    }

    @Override
    public <T1, T2, T3> ResultUtils.ResultsWith3<T1, T2, T3> findEntitiesWith(Class<T1> type1, Class<T2> type2, Class<T3> type3) {
        return ResultUtils.ResultsWith3.of(this.dominion.findEntitiesWith(type1, type2, type3));
    }

    @Override
    public <T1, T2, T3, T4> ResultUtils.ResultsWith4<T1, T2, T3, T4> findEntitiesWith(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4) {
        return ResultUtils.ResultsWith4.of(this.dominion.findEntitiesWith(type1, type2, type3, type4));
    }

    @Override
    public <T1, T2, T3, T4, T5> ResultUtils.ResultsWith5<T1, T2, T3, T4, T5> findEntitiesWith(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5) {
        return ResultUtils.ResultsWith5.of(this.dominion.findEntitiesWith(type1, type2, type3, type4, type5));
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> ResultUtils.ResultsWith6<T1, T2, T3, T4, T5, T6> findEntitiesWith(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5, Class<T6> type6) {
        return ResultUtils.ResultsWith6.of(this.dominion.findEntitiesWith(type1, type2, type3, type4, type5, type6));
    }

    public Results<? extends Entity> findEntitiesWith(Framework framework) {
        return framework.findWith(this);
    }

    public Results<? extends Entity> findEntitiesWith(Framework framework, Class<?> ...components) {
        return framework.findWith(this, components);
    }

    @Override
    public boolean isClosed() {
        return this.dominion.isClosed();
    }

    @Override
    public void close() {
        this.dominion.close();
    }
}
