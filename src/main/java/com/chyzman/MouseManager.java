package com.chyzman;

import com.chyzman.object.Camera;
import org.lwjgl.glfw.GLFW;

public class MouseManager {
    private boolean firstMouse = true;
    private boolean grabbed = false;

    private double lastX = 400, lastY = 300;

    public void setCursorPos(double mouseX, double mouseY) {
        if (!grabbed)
            return;
        if (firstMouse) {
            lastX = mouseX;
            lastY = mouseY;
            firstMouse = false;
        }
        Camera camera = Game.GAME.camera;

        double xOffset = mouseX - lastX;
        double yOffset = lastY - mouseY;
        lastX = mouseX;
        lastY = mouseY;

        float sensitivity = 0.1f;
        xOffset *= sensitivity;
        yOffset *= sensitivity;

        camera.yaw += xOffset;
        camera.pitch += yOffset;
    }

    public void toggleGrabbed() {
        grabbed = !grabbed;
        if (grabbed)
            GLFW.glfwSetInputMode(Game.window.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        else
            GLFW.glfwSetInputMode(Game.window.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }
}
