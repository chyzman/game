package com.chyzman.util;

import io.wispforest.endec.Endec;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public enum Direction {
    DOWN(1, new Vector3f(0, -1, 0)),
    UP(0, new Vector3f(0, 1, 0)),
    NORTH(3, new Vector3f(0, 0, -1)),
    SOUTH(2, new Vector3f(0, 0, 1)),
    WEST(5, new Vector3f(-1, 0, 0)),
    EAST(4, new Vector3f(1, 0, 0));

    public static final Direction[] DIRECTIONS = values();
    public static final Endec<Direction> ENDEC = Endec.forEnum(Direction.class);

    private final int idOpposite;
    private final Vector3fc normal;

    Direction(int idOpposite, Vector3fc normal) {
        this.idOpposite = idOpposite;
        this.normal = normal;
    }

    public int idOpposite() {
        return idOpposite;
    }

    public Vector3fc normal() {
        return normal;
    }

    public static Direction fromYaw(int yaw) {
        if (yaw == 3) return Direction.NORTH;
        if (yaw == 1) return Direction.SOUTH;
        if (yaw == 0) return Direction.EAST;
        if (yaw == 2) return Direction.WEST;

        return null;
    }
}
