package com.chyzman.object;

import com.chyzman.Game;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera extends GameObject {
    public Vector3d deltaPos = new Vector3d();
    public Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
    public Vector3f cameraUp    = new Vector3f(0.0f, 1.0f,  0.0f);
    public float pitch, yaw;

    @Override
    public void update() {
        Matrix4f transform = Game.window.getViewMatrix();
//        pos(pos.add(deltaPos));
        var cameraPos = new Vector3f((float) pos.x, (float) pos.y, (float) pos.z);

        var view = new Matrix4f();

        Vector3f front = new Vector3f();
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cameraFront = front.normalize();
        transform.set(getViewMatrix());

        long window = Game.window.window;
        float cameraSpeed = 2.5f * Game.GAME.deltaTime;

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            pos.add(new Vector3f(cameraFront).mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            pos.sub(new Vector3f(cameraFront).mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            pos.sub(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            pos.add(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            pos.add(0, cameraSpeed, 0);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            pos.add(0, -cameraSpeed, 0);
        }
    }



    public Matrix4f getViewMatrix() {
        var cameraPos = new Vector3f((float) pos.x, (float) pos.y, (float) pos.z);
        return new Matrix4f().lookAt(cameraPos, new Vector3f(cameraPos).add(cameraFront), cameraUp);
    }
}