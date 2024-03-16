package com.chyzman.object;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class CameraConfiguration {
    public Vector3d deltaPos = new Vector3d();
    public Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
    public Vector3f cameraUp    = new Vector3f(0.0f, 1.0f,  0.0f);
    public float fov = 90;
    public float cameraSpeed = 2.5f;
    public float pitch, yaw;

    public Matrix4f getViewMatrix(Vector3d pos) {
        var cameraPos = new Vector3f((float) pos.x, (float) pos.y, (float) pos.z);
        return new Matrix4f().lookAt(cameraPos, new Vector3f(cameraPos).add(cameraFront), cameraUp);
    }
}