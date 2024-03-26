package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.IdentifiedSystem;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.render.Renderer;
import com.chyzman.util.Id;
import com.chyzman.util.UtilUtil;
import com.jme3.bullet.objects.PhysicsRigidBody;
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
                var pos = entity.get(Position.class);
                var rotation = entity.get(Rotation.class);
                Matrix4f transform = Game.renderer.getViewMatrix().peek();
                if (cameraConfig.freecam) {
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
                    }
                    if (pos != null) {

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
                } else if (cameraConfig.target != null) {
                    var target = cameraConfig.target;
                    Vector3d targetPos;
                    Quaternionf targetRotation;
                    if (target.has(PhysicsRigidBody.class)) {
                        var body = target.get(PhysicsRigidBody.class);
                        var bodyTransform = body.getTransform(null);
                        targetPos = new Vector3d(
                                bodyTransform.getTranslation().x,
                                bodyTransform.getTranslation().y,
                                bodyTransform.getTranslation().z
                        );
                        targetRotation = new Quaternionf(
                                bodyTransform.getRotation().getX(),
                                bodyTransform.getRotation().getY(),
                                bodyTransform.getRotation().getZ(),
                                bodyTransform.getRotation().getW()
                        );
                    } else {
                        targetPos = UtilUtil.thisOr(target.get(Position.class), new Vector3d());
                        targetRotation = UtilUtil.thisOr(target.get(Rotation.class), new Quaternionf());
                    }
                    pos.set(targetPos).negate().add(new Vector3d(transform.positiveY(new Vector3f()).mul(-2).sub(0,0,4)));
                    rotation.set(targetRotation);
                }
                if (rotation != null) transform.rotation(rotation);
                if (pos != null) transform.translate(new Vector3f((float) pos.x, (float) pos.y, (float) pos.z));
            });
        }
    }
}