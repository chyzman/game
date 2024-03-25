package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.IdentifiedSystem;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.render.Renderer;
import com.chyzman.util.Id;
import dev.dominion.ecs.api.Dominion;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class CameraControl {

    public static IdentifiedSystem<FramedDominion> create(FramedDominion dominion, Supplier<Double> deltaTime) {
        return IdentifiedSystem.of(new Id("game", "camera_control"), dominion, deltaTime, CameraControl::update);
    }

    private static void update(Dominion dominion, double deltaTime) {
        if (Game.window.mouseGrabbed()) {
            for (var entityResult : dominion.findEntitiesWith(Position.class, Rotation.class, CameraConfiguration.class)) {
                var pos = entityResult.comp1();
                var rotation = entityResult.comp2();
                var camera = entityResult.comp3();

                Matrix4f transform = Game.renderer.getViewMatrix().peek();

                var fpos = new Vector3f();
                var matr = new Matrix4f()/*.translate(fpos)*/.rotate(rotation);
                transform.rotation(rotation).translate(new Vector3f((float) pos.x, (float) pos.y, (float) pos.z)); //.add(matr);

                long window = Game.window.handle;

                float localCameraSpeed = (float) (camera.cameraSpeed * deltaTime);
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                    pos.add(new Vector3d(transform.positiveZ(new Vector3f()).mul(localCameraSpeed)));
                }
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                    pos.add(new Vector3d(transform.positiveZ(new Vector3f()).mul(-localCameraSpeed)));
                }
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                    pos.add(new Vector3d(transform.positiveX(new Vector3f()).mul(localCameraSpeed)));
                }
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                    pos.add(new Vector3d(transform.positiveX(new Vector3f()).mul(-localCameraSpeed)));
                }
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
                    pos.add(new Vector3d(transform.positiveY(new Vector3f()).mul(-localCameraSpeed)));
                }
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
                    pos.add(new Vector3d(transform.positiveY(new Vector3f()).mul(localCameraSpeed)));
                }
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
                    rotation.rotateLocalZ((float) (-camera.rotationSpeed * deltaTime));
                }
                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
                    rotation.rotateLocalZ((float) (camera.rotationSpeed * deltaTime));
                }
            }
        }
    }

    /*
        Camera: (Pos, CameraConfiguration)
        CameraConfiguration: (Spd, Pitch, Yaw, Offset, EntityRef)
        Player: (Pos, Vel)
     */
}