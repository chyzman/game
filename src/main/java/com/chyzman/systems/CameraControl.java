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
import dev.dominion.ecs.api.Entity;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class CameraControl {
    public static boolean first = true;
    private static Vector2i lastMousePos = new Vector2i(0,0);

    public static IdentifiedSystem<FramedDominion> create(FramedDominion dominion, Supplier<Double> deltaTime) {
        return IdentifiedSystem.of(new Id("game", "camera_control"), dominion, deltaTime, CameraControl::update);
    }

    private static void update(FramedDominion dominion, double deltaTime) {
        var window = Game.window;
        if (window.mouseGrabbed()) {
            dominion.findEntitiesWith(CameraConfiguration.class).forAll((entity, cameraConfig) -> {
                if (cameraConfig.freecam) {
                    var pos = entity.get(Position.class);
                    var rotation = entity.get(Rotation.class);

                    Matrix4f transform = Game.renderer.getViewMatrix().peek();

                    if (rotation != null) {
                        var offset = new Vector2d(lastMousePos).sub(new Vector2f(window.mousePos()));
                        if (first) {
                            offset.set(0, 0);
                            first = false;
                        }
                        lastMousePos.set(window.mousePos());
                        offset.mul(cameraConfig.sensitivity);

                        rotation.rotateY((float) -offset.x);
                        rotation.rotateLocalX((float) -offset.y);
                        transform.rotation(rotation);
                    }
                    if (pos != null) {
                        transform.translate(new Vector3f((float) pos.x, (float) pos.y, (float) pos.z));

                        var handle = window.handle;

                        float localCameraSpeed = (float) (cameraConfig.cameraSpeed * deltaTime);
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                            pos.add(new Vector3d(transform.positiveZ(new Vector3f()).mul(localCameraSpeed)));
                        }
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                            pos.add(new Vector3d(transform.positiveZ(new Vector3f()).mul(-localCameraSpeed)));
                        }
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                            pos.add(new Vector3d(transform.positiveX(new Vector3f()).mul(localCameraSpeed)));
                        }
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                            pos.add(new Vector3d(transform.positiveX(new Vector3f()).mul(-localCameraSpeed)));
                        }
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
                            pos.add(new Vector3d(transform.positiveY(new Vector3f()).mul(-localCameraSpeed)));
                        }
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
                            pos.add(new Vector3d(transform.positiveY(new Vector3f()).mul(localCameraSpeed)));
                        }
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
                            rotation.rotateLocalZ((float) (-cameraConfig.rotationSpeed * deltaTime));
                        }
                        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
                            rotation.rotateLocalZ((float) (cameraConfig.rotationSpeed * deltaTime));
                        }
                    }
                }
            });
        }
    }
}