package com.chyzman.object;

import dev.dominion.ecs.api.Entity;

public class CameraConfiguration {
    public float fov = 90;
    public float cameraSpeed = 2.5f;
    public float rotationSpeed = 1.0f;
    public float sensitivity = 0.005f;
    public boolean freecam = false;
    public Entity target;
}