package com.chyzman.object;

import com.chyzman.object.components.Component;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObject {
    protected final List<Component> components = new ArrayList<>();
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