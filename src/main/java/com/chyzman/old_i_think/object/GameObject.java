package com.chyzman.old_i_think.object;

import org.joml.Vector3d;

public abstract class GameObject {
    protected Vector3d pos = new Vector3d();

    public Vector3d pos() {
        return pos;
    }

    public GameObject pos(Vector3d pos) {
        this.pos = pos;
        return this;
    }

    public abstract void update();
}