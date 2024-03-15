package com.chyzman.component.position;

import org.joml.Vector3d;

public class Velocity extends Vector3d{
    public Velocity() {
    }

    public Velocity(Number n) {
        super(n.doubleValue(), n.doubleValue(), n.doubleValue());
    }

    public Velocity(Number x, Number y, Number z) {
        super(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }
}