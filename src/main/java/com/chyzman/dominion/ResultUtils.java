package com.chyzman.dominion;

import dev.dominion.ecs.api.Entity;
import dev.dominion.ecs.api.Results;
import io.wispforest.endec.functions.QuadConsumer;
import io.wispforest.endec.util.TriConsumer;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class ResultUtils {

    public interface ResultsWith1<T1> extends Results<Results.With1<T1>>, Iterable<Results.With1<T1>> {

        default void forAll(BiConsumer<Entity, T1> consumer){
            stream().forEach(with -> consumer.accept(with.entity(), with.comp()));
        }

        default Iterable<T1> comp1Iterable() { return unpackingIterable(this, With1::comp); }

        static <T1> ResultsWith1<T1> of(Results<With1<T1>> results){
            return new ResultsWith1<>() {
                @Override public Iterator<With1<T1>> iterator() { return results.iterator(); }
                @Override public Stream<With1<T1>> stream() { return results.stream(); }
                @Override public Stream<With1<T1>> parallelStream() { return results.parallelStream(); }
                @Override public Results<With1<T1>> without(Class<?>... componentTypes) { return results.without(componentTypes); }
                @Override public Results<With1<T1>> withAlso(Class<?>... componentTypes) { return results.withAlso(componentTypes); }
                @Override public <S extends Enum<S>> Results<With1<T1>> withState(S state) { return results.withState(state); }
            };
        }
    }

    public interface ResultsWith2<T1, T2> extends Results<Results.With2<T1, T2>>{
        default void forAll(TriConsumer<Entity, T1, T2> consumer) {
            stream().forEach(with -> consumer.accept(with.entity(), with.comp1(), with.comp2()));
        }

        default Iterable<T1> comp1Iterable() { return unpackingIterable(this, With2::comp1); }

        default Iterable<T2> comp2Iterable() { return unpackingIterable(this, With2::comp2); }

        static <T1, T2> ResultsWith2<T1, T2> of(Results<With2<T1, T2>> results){
            return new ResultsWith2<>() {
                @Override public Iterator<With2<T1, T2>> iterator() { return results.iterator(); }
                @Override public Stream<With2<T1, T2>> stream() { return results.stream(); }
                @Override public Stream<With2<T1, T2>> parallelStream() { return results.parallelStream(); }
                @Override public Results<With2<T1, T2>> without(Class<?>... componentTypes) { return results.without(componentTypes); }
                @Override public Results<With2<T1, T2>> withAlso(Class<?>... componentTypes) { return results.withAlso(componentTypes); }
                @Override public <S extends Enum<S>> Results<With2<T1, T2>> withState(S state) { return results.withState(state); }
            };
        }
    }

    public interface ResultsWith3<T1, T2, T3> extends Results<Results.With3<T1, T2, T3>> {
        default void forAll(QuadConsumer<Entity, T1, T2, T3> consumer){
            stream().forEach(with -> consumer.accept(with.entity(), with.comp1(), with.comp2(), with.comp3()));
        }

        default Iterable<T1> comp1Iterable() { return unpackingIterable(this, With3::comp1); }

        default Iterable<T2> comp2Iterable() { return unpackingIterable(this, With3::comp2); }

        default Iterable<T3> comp3Iterable() { return unpackingIterable(this, With3::comp3); }

        static <T1, T2, T3> ResultsWith3<T1, T2, T3> of(Results<With3<T1, T2, T3>> results){
            return new ResultsWith3<>() {
                @Override public Iterator<With3<T1, T2, T3>> iterator() { return results.iterator(); }
                @Override public Stream<With3<T1, T2, T3>> stream() { return results.stream(); }
                @Override public Stream<With3<T1, T2, T3>> parallelStream() { return results.parallelStream(); }
                @Override public Results<With3<T1, T2, T3>> without(Class<?>... componentTypes) { return results.without(componentTypes); }
                @Override public Results<With3<T1, T2, T3>> withAlso(Class<?>... componentTypes) { return results.withAlso(componentTypes); }
                @Override public <S extends Enum<S>> Results<With3<T1, T2, T3>> withState(S state) { return results.withState(state); }
            };
        }
    }

    public interface ResultsWith4<T1, T2, T3, T4> extends Results<Results.With4<T1, T2, T3, T4>> {
        default void forAll(Consumer5<Entity, T1, T2, T3, T4> consumer){
            stream().forEach(with -> consumer.accept(with.entity(), with.comp1(), with.comp2(), with.comp3(), with.comp4()));
        }

        default Iterable<T1> comp1Iterable() { return unpackingIterable(this, With4::comp1); }

        default Iterable<T2> comp2Iterable() { return unpackingIterable(this, With4::comp2); }

        default Iterable<T3> comp3Iterable() { return unpackingIterable(this, With4::comp3); }

        default Iterable<T4> comp4Iterable() { return unpackingIterable(this, With4::comp4); }

        static <T1, T2, T3, T4> ResultsWith4<T1, T2, T3, T4> of(Results<With4<T1, T2, T3, T4>> results){
            return new ResultsWith4<>() {
                @Override public Iterator<With4<T1, T2, T3, T4>> iterator() { return results.iterator(); }
                @Override public Stream<With4<T1, T2, T3, T4>> stream() { return results.stream(); }
                @Override public Stream<With4<T1, T2, T3, T4>> parallelStream() { return results.parallelStream(); }
                @Override public Results<With4<T1, T2, T3, T4>> without(Class<?>... componentTypes) { return results.without(componentTypes); }
                @Override public Results<With4<T1, T2, T3, T4>> withAlso(Class<?>... componentTypes) { return results.withAlso(componentTypes); }
                @Override public <S extends Enum<S>> Results<With4<T1, T2, T3, T4>> withState(S state) { return results.withState(state); }
            };
        }
    }

    public interface ResultsWith5<T1, T2, T3, T4, T5> extends Results<Results.With5<T1, T2, T3, T4, T5>> {
        default void forAll(Consumer6<Entity, T1, T2, T3, T4, T5> consumer){
            stream().forEach(with -> consumer.accept(with.entity(), with.comp1(), with.comp2(), with.comp3(), with.comp4(), with.comp5()));
        }

        default Iterable<T1> comp1Iterable() { return unpackingIterable(this, With5::comp1); }

        default Iterable<T2> comp2Iterable() { return unpackingIterable(this, With5::comp2); }

        default Iterable<T3> comp3Iterable() { return unpackingIterable(this, With5::comp3); }

        default Iterable<T4> comp4Iterable() { return unpackingIterable(this, With5::comp4); }

        default Iterable<T5> comp5Iterable() { return unpackingIterable(this, With5::comp5); }

        static <T1, T2, T3, T4, T5> ResultsWith5<T1, T2, T3, T4, T5> of(Results<With5<T1, T2, T3, T4, T5>> results){
            return new ResultsWith5<>() {
                @Override public Iterator<With5<T1, T2, T3, T4, T5>> iterator() { return results.iterator(); }
                @Override public Stream<With5<T1, T2, T3, T4, T5>> stream() { return results.stream(); }
                @Override public Stream<With5<T1, T2, T3, T4, T5>> parallelStream() { return results.parallelStream(); }
                @Override public Results<With5<T1, T2, T3, T4, T5>> without(Class<?>... componentTypes) { return results.without(componentTypes); }
                @Override public Results<With5<T1, T2, T3, T4, T5>> withAlso(Class<?>... componentTypes) { return results.withAlso(componentTypes); }
                @Override public <S extends Enum<S>> Results<With5<T1, T2, T3, T4, T5>> withState(S state) { return results.withState(state); }
            };
        }
    }

    public interface ResultsWith6<T1, T2, T3, T4, T5, T6> extends Results<Results.With6<T1, T2, T3, T4, T5, T6>> {
        default void forAll(Consumer7<Entity, T1, T2, T3, T4, T5, T6> consumer){
            stream().forEach(with -> consumer.accept(with.entity(), with.comp1(), with.comp2(), with.comp3(), with.comp4(), with.comp5(), with.comp6()));
        }

        default Iterable<T1> comp1Iterable() { return unpackingIterable(this, With6::comp1); }

        default Iterable<T2> comp2Iterable() { return unpackingIterable(this, With6::comp2); }

        default Iterable<T3> comp3Iterable() { return unpackingIterable(this, With6::comp3); }

        default Iterable<T4> comp4Iterable() { return unpackingIterable(this, With6::comp4); }

        default Iterable<T5> comp5Iterable() { return unpackingIterable(this, With6::comp5); }

        default Iterable<T6> comp6Iterable() { return unpackingIterable(this, With6::comp6); }

        static <T1, T2, T3, T4, T5, T6> ResultsWith6<T1, T2, T3, T4, T5, T6> of(Results<With6<T1, T2, T3, T4, T5, T6>> results){
            return new ResultsWith6<>() {
                @Override public Iterator<With6<T1, T2, T3, T4, T5, T6>> iterator() { return results.iterator(); }
                @Override public Stream<With6<T1, T2, T3, T4, T5, T6>> stream() { return results.stream(); }
                @Override public Stream<With6<T1, T2, T3, T4, T5, T6>> parallelStream() { return results.parallelStream(); }
                @Override public Results<With6<T1, T2, T3, T4, T5, T6>> without(Class<?>... componentTypes) { return results.without(componentTypes); }
                @Override public Results<With6<T1, T2, T3, T4, T5, T6>> withAlso(Class<?>... componentTypes) { return results.withAlso(componentTypes); }
                @Override public <S extends Enum<S>> Results<With6<T1, T2, T3, T4, T5, T6>> withState(S state) { return results.withState(state); }
            };
        }
    }

    public interface Consumer5<A, B, C, D, E> {
        void accept(A a, B b, C c, D d, E e);
    }

    public interface Consumer6<A, B, C, D, E, F> {
        void accept(A a, B b, C c, D d, E e, F f);
    }

    public interface Consumer7<A, B, C, D, E, F, G> {
        void accept(A a, B b, C c, D d, E e, F f, G g);
    }

    public static <T, C> Iterable<C> unpackingIterable(Iterable<T> iterable, Function<T, C> unpackFunc) {
        return () -> new UnpackingIterator<>(iterable.iterator(), unpackFunc);
    }

    public static class UnpackingIterator<T, C> implements Iterator<C> {
        private final Function<T, C> unpackFunc;
        private final Iterator<T> iterator;

        public UnpackingIterator(Iterator<T> iterator, Function<T, C> unpackFunc){
            this.iterator = iterator;
            this.unpackFunc = unpackFunc;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public C next() {
            return unpackFunc.apply(iterator.next());
        }
    }
}
