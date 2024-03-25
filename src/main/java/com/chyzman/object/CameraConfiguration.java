package com.chyzman.object;

import dev.dominion.ecs.api.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class CameraConfiguration {
    public float fov = 90;
    public float cameraSpeed = 2.5f;
    public float sensitivity = 0.01f;
    public Entity target;
}