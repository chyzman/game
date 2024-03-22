package com.chyzman;

import com.chyzman.component.position.Position;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.object.CameraConfiguration;
import org.lwjgl.glfw.GLFW;

public class MouseManager {
    private boolean firstMouse = true;
    private boolean grabbed = false;

    private double lastX, lastY;

    public void setCursorPos(FramedDominion dominion, double mouseX, double mouseY) {
        if (!grabbed) return;

        if (firstMouse) {
            lastX = mouseX;
            lastY = mouseY;
            firstMouse = false;
        }

        for (var camera : dominion.findEntitiesWith(Position.class, CameraConfiguration.class).comp2Iterable()) {
            double xOffset = mouseX - lastX;
            double yOffset = lastY - mouseY;
            lastX = mouseX;
            lastY = mouseY;

            float sensitivity = 0.1f;
            xOffset *= sensitivity;
            yOffset *= sensitivity;

            camera.yaw += xOffset;
            camera.pitch += yOffset;

            if (camera.pitch >= 89F) camera.pitch = 89F;
            if (camera.pitch <= -89F) camera.pitch = -89F;
        }
    }

    public void toggleGrabbed() {
        grabbed = !grabbed;
        lastX = (double) Game.window.width / 2; lastY = (double) Game.window.height / 2;
        if (grabbed) {
            GLFW.glfwSetCursorPos(Game.window.handle, (double) Game.window.width / 2, (double) Game.window.height / 2);
            GLFW.glfwSetInputMode(Game.window.handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        } else {
            GLFW.glfwSetCursorPos(Game.window.handle, (double) Game.window.width / 2, (double) Game.window.height / 2);
            GLFW.glfwSetInputMode(Game.window.handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }
}