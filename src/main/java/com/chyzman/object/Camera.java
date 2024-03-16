package com.chyzman.object;

import com.chyzman.Game;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    public Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
    public Vector3f cameraUp    = new Vector3f(0.0f, 1.0f,  0.0f);
    public float fov = 90;
    public float cameraSpeed = 2.5f;
    public float pitch, yaw;

    public void update() {
        Matrix4f transform = Game.window.getViewMatrix();

        Vector3f pos = Game.GAME.cameraEntity.get(Vector3f.class);

        Vector3f front = new Vector3f();
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cameraFront = front.normalize();
        transform.set(getViewMatrix(pos));

        long window = Game.window.window;


        float localCameraSpeed = (float) (cameraSpeed * Game.GAME.clientScheduler.deltaTime());
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            pos.add(new Vector3f(cameraFront).mul(localCameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            pos.sub(new Vector3f(cameraFront).mul(localCameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            pos.sub(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(localCameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            pos.add(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(localCameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            pos.add(0, localCameraSpeed, 0);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            pos.add(0, -localCameraSpeed, 0);
        }
    }



    public Matrix4f getViewMatrix(Vector3f pos) {
        var cameraPos = new Vector3f(pos.x, pos.y, pos.z);
        return new Matrix4f().lookAt(cameraPos, new Vector3f(cameraPos).add(cameraFront), cameraUp);
    }
}