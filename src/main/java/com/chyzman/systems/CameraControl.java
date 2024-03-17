package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.position.Position;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.render.Renderer;
import dev.dominion.ecs.api.Dominion;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class CameraControl {

    public static DomSystem create(Dominion dominion, Supplier<Double> deltaTime){
        return DomSystem.create(dominion, "camera_control", deltaTime, CameraControl::update);
    }

    private static void update(Dominion dominion, double deltaTime){
        for (var entityResult : dominion.findEntitiesWith(Position.class, CameraConfiguration.class)) {
            var pos = entityResult.comp1();
            Renderer.cameraPosition = pos;
            var camera = entityResult.comp2();

            var yaw = camera.yaw;
            var pitch = camera.pitch;

            Matrix4f transform = Game.window.getViewMatrix().peek();

            Vector3f front = new Vector3f();
            front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            front.y = (float) Math.sin(Math.toRadians(pitch));
            front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            camera.cameraFront = front.normalize();
            transform.set(camera.getViewMatrix(pos));

            long window = Game.window.handle;

            float localCameraSpeed = (float) (camera.cameraSpeed * deltaTime);
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                pos.add(new Vector3f(camera.cameraFront).mul(localCameraSpeed));
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                pos.sub(new Vector3f(camera.cameraFront).mul(localCameraSpeed));
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                pos.sub(new Vector3f(camera.cameraFront).cross(camera.cameraUp).normalize().mul(localCameraSpeed));
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                pos.add(new Vector3f(camera.cameraFront).cross(camera.cameraUp).normalize().mul(localCameraSpeed));
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
                pos.add(0, localCameraSpeed, 0);
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
                pos.add(0, -localCameraSpeed, 0);
            }
        }
    }

    /*
        Camera: (Pos, CameraConfiguration)
        CameraConfiguration: (Spd, Pitch, Yaw, Offset, EntityRef)
        Player: (Pos, Vel)
     */
}