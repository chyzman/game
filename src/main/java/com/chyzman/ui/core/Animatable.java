package com.chyzman.ui.core;

public interface Animatable<T extends Animatable<T>> {

    T interpolate(T next, float delta);

}
