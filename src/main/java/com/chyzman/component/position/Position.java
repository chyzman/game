package com.chyzman.component.position;

import org.joml.Vector3d;

public class Position extends Vector3d{
    public Position() {
    }

    public Position(Number n) {
        super(n.doubleValue(), n.doubleValue(), n.doubleValue());
    }

    public Position(Number x, Number y, Number z) {
        super(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }
}