package com.chyzman;

import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.object.CameraConfiguration;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

public class MouseManager {
    private boolean grabbed = false;

    private Vector3i last = new Vector3i(0,0, 0);

    public void setCursorPos(FramedDominion dominion, double mouseX, double mouseY) {
        if (!grabbed) return;

        dominion.findEntitiesWith(Rotation.class, CameraConfiguration.class).forAll((entity, rotation, cameraConfiguration) -> {
            double xOffset = mouseX - last.x;
            double yOffset = last.y - mouseY;
            last.set((int) mouseX, (int) mouseY, 0);

            xOffset *= cameraConfiguration.sensitivity;
            yOffset *= cameraConfiguration.sensitivity;

            rotation.rotateY((float) xOffset);
            rotation.rotateLocalX((float) -yOffset);
        });
    }

    public void toggleGrabbed() {
        grabbed = !grabbed;
        last.set(Game.window.width / 2, Game.window.height / 2, 0);
        if (grabbed) {
            GLFW.glfwSetCursorPos(Game.window.handle, (double) Game.window.width / 2, (double) Game.window.height / 2);
            GLFW.glfwSetInputMode(Game.window.handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        } else {
            GLFW.glfwSetCursorPos(Game.window.handle, (double) Game.window.width / 2, (double) Game.window.height / 2);
            GLFW.glfwSetInputMode(Game.window.handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    public boolean isGrabbed() {
        return grabbed;
    }
}