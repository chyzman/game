package com.chyzman.old_i_think.object;

import org.joml.Vector3d;

public class Camera extends GameObject {
    public Vector3d deltaPos = new Vector3d();
    public float pitch, yaw;

    @Override
    public void update() {
        pos(pos.add(deltaPos));
    }
}