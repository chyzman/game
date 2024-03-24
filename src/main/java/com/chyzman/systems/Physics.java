package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.Named;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.IdentifiedSystem;
import com.chyzman.util.Id;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.NativeLibraryLoader;
import org.joml.Quaterniond;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.chyzman.Game.LOGGER;
import static com.chyzman.Game.LOGIC_TICK_RATE;

public class Physics {
    public static PhysicsSpace physicsSpace;
    public static int lockOut = 0;

    public static IdentifiedSystem<FramedDominion> create(FramedDominion dominion, Supplier<Double> deltaTime) {
        NativeLibraryLoader.loadLibbulletjme(true, Path.of("src/main/resources/native").toFile(), "Debug", "Sp");
        physicsSpace = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        return IdentifiedSystem.of(new Id("game", "physics"), dominion, deltaTime, Physics::update)
                .safe(64, (framedDominion, id, e) -> LOGGER.error("Error occured with the given system! [Id: " + id + "]", e));
    }

    public static void update(FramedDominion dom, double deltaTime) {
        physicsSpace.update(1f / LOGIC_TICK_RATE);

        for (var result : dom.findEntitiesWith(PhysicsRigidBody.class, Position.class)) {
            var body = result.comp1();
            var pos = result.comp2();
            var p = body.getPhysicsLocation(new Vector3f());
            pos.set(p.x, p.y, p.z);
        }

        for (var result : dom.findEntitiesWith(PhysicsRigidBody.class, Rotation.class)) {
            var body = result.comp1();
            var rotation = result.comp2();
            var q = body.getPhysicsRotation(new Quaternion());
            rotation.set(new Quaterniond(q.getX(), q.getY(), q.getZ(), q.getW()));
        }


            if (--lockOut <= 0 && !Game.window.mouseGrabbed()) {
                dom.findEntitiesWith(Named.class, PhysicsRigidBody.class).forAll((entity, named, body) -> {
                    if (!named.hasName() || !named.name().equals("player")) return;

                    long window = Game.window.handle;

                    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                        body.applyImpulse(new Vector3f(0f, 0, -1f), new Vector3f(0, 0, 0));
                        lockOut = LOGIC_TICK_RATE / 4;
                    }

                    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                        body.applyImpulse(new Vector3f(0f, 0, 1f), new Vector3f(0, 0, 0));
                        lockOut = LOGIC_TICK_RATE / 4;
                    }

                    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                        body.applyImpulse(new Vector3f(-1f, 0, 0), new Vector3f(0, 0, 0));
                        lockOut = LOGIC_TICK_RATE / 4;
                    }

                    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                        body.applyImpulse(new Vector3f(1f, 0, 0), new Vector3f(0, 0, 0));
                        lockOut = LOGIC_TICK_RATE / 4;
                    }


                    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
                        body.applyImpulse(new Vector3f(0, 8f, 0), new Vector3f(0, 0, 0));
                        lockOut = LOGIC_TICK_RATE / 4;
                    }

                    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
                        body.setPhysicsLocation(new Vector3f(0, 10, 0));
                        body.setPhysicsRotation(new Quaternion());
                        body.setLinearVelocity(new Vector3f());
                        body.setAngularVelocity(new Vector3f());
                        body.setEnableSleep(false);
                        lockOut = LOGIC_TICK_RATE / 4;
                    }
                });
            }
    }
}