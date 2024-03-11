package com.chyzman.object;

import org.joml.Vector2d;

public abstract class GameObject {
    protected Vector2d pos = new Vector2d();

    public Vector2d pos() {
        return pos;
    }

    public GameObject pos(Vector2d pos) {
        this.pos = pos;
        return this;
    }

    public abstract void update();
}