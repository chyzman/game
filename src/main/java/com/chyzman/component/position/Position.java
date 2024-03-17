package com.chyzman.component.position;

import org.joml.Vector3d;

import java.util.List;
import java.util.function.Function;

public class Position extends Vector3d {
    public List<Function<Vector3d, Vector3d>> queue;

    public Position() {
    }

    public Position(Number n) {
        super(n.doubleValue(), n.doubleValue(), n.doubleValue());
    }

    public Position(Number x, Number y, Number z) {
        super(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }
}