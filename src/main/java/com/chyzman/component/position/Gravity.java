package com.chyzman.component.position;

import org.joml.Vector3d;

public class Gravity extends Vector3d {
    public Gravity() {
    }

    public static Gravity zeroGrav() {
        return new Gravity(0);
    }

    public Gravity(Number n) {
        super(n.doubleValue(), n.doubleValue(), n.doubleValue());
    }

    public Gravity(Number x, Number y, Number z) {
        super(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }
}