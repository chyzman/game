package com.chyzman.util;

import io.wispforest.endec.Endec;

public enum Direction {
    DOWN(1, new Vec3i(0, -1, 0)),
    UP(0, new Vec3i(0, 1, 0)),
    NORTH(3, new Vec3i(0, 0, -1)),
    SOUTH(2, new Vec3i(0, 0, 1)),
    WEST(5, new Vec3i(-1, 0, 0)),
    EAST(4, new Vec3i(1, 0, 0));

    public static final Direction[] DIRECTIONS = values();
    public static final Endec<Direction> ENDEC = Endec.forEnum(Direction.class);

    private final int idOpposite;
    private final Vec3i normal;

    Direction(int idOpposite, Vec3i normal) {
        this.idOpposite = idOpposite;
        this.normal = normal;
    }

    public int id() {
        return ordinal();
    }

    public int idOpposite() {
        return idOpposite;
    }

    public Vec3i normal() {
        return normal;
    }
}
