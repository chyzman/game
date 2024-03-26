package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.Named;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.Frameworks;
import com.chyzman.dominion.IdentifiedSystem;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.components.MeshComponent;
import com.chyzman.util.Id;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.objects.PhysicsBody;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.NativeLibraryLoader;
import io.wispforest.endec.util.MutableHolder;
import org.joml.Quaterniond;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;

import static com.chyzman.Game.LOGGER;
import static com.chyzman.Game.LOGIC_TICK_RATE;

public class Physics {
    public static PhysicsSpace physicsSpace = null;
    public static int lockOut = 0;

    public static IdentifiedSystem<FramedDominion> create(FramedDominion dominion, Supplier<Double> deltaTime) {
        NativeLibraryLoader.loadLibbulletjme(true, Path.of("src/main/resources/native").toFile(), "Debug", "Sp");

        return IdentifiedSystem.of(new Id("game", "physics"), dominion, deltaTime, Physics::update)
                .safe(64, (framedDominion, id, e) -> LOGGER.error("Error occured with the given system! [Id: " + id + "]", e));
    }

    public static void init(FramedDominion dominion) {
        var plane = new Plane(Vector3f.UNIT_Y, 0);
        var planeCollision = new PlaneCollisionShape(plane);
        var floor = new PhysicsRigidBody(planeCollision, PhysicsBody.massForStatic);
        physicsSpace.addCollisionObject(floor);

        var playerCollision = new CapsuleCollisionShape(0.15f, 1.7f);
//        var playerBox = new PhysicsRigidBody(playerCollision, 1);
        var player = new PhysicsCharacter(playerCollision, 0.1f);

        physicsSpace.addCollisionObject(player);

        var holder = new MutableHolder<CameraConfiguration>();

        dominion.findEntitiesWith(Named.class, CameraConfiguration.class).forAll((entity, named, cameraConfiguration) -> {
            if(named.hasName() && Objects.equals(named.name(), "camera")){
                holder.setValue(cameraConfiguration);
            }
        });

        var entity = dominion.createEntity(
                Frameworks.PHYSICS_ENTITY,
                new Named("player"),
                player
        );

        Game.GAME.runOnReaderThread(() -> {
            entity.add(MeshComponent.obj(new Id("game", "chyzman.obj"), new Id("game", "chyzman.png")));
        });

        holder.getValue().target = entity;
    }

    public static void update(FramedDominion dom, double deltaTime) {
        if(physicsSpace == null) {
            physicsSpace = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);

            init(dom);
        }

        physicsSpace.update(1f / LOGIC_TICK_RATE);

        for (var result : dom.findEntitiesWith(PhysicsCharacter.class, Position.class)) {
            var body = result.comp1();
            var pos = result.comp2();
            var p = body.getPhysicsLocation(new Vector3f());
            pos.set(p.x, p.y, p.z);
        }

        for (var result : dom.findEntitiesWith(PhysicsCharacter.class, Rotation.class)) {
            var body = result.comp1();
            var rotation = result.comp2();
            var q = body.getPhysicsRotation(new Quaternion());
            rotation.set(new Quaterniond(q.getX(), q.getY(), q.getZ(), q.getW()));
        }

        if (--lockOut <= 0 && Game.window.mouseGrabbed() && !Game.camera.get(CameraConfiguration.class).freecam) {
            dom.findEntitiesWith(Named.class, PhysicsCharacter.class).forAll((entity, named, body) -> {
                if (!named.hasName() || !named.name().equals("player")) return;

                long window = Game.window.handle;

                Vector3f walkDirection = new Vector3f();

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                    walkDirection = walkDirection.add(new Vector3f(0, 0, -0.1f));
                }

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                    walkDirection = walkDirection.add(new Vector3f(0, 0, 0.1f));
                }

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                    walkDirection = walkDirection.add(new Vector3f(-0.1f, 0, 0));
                }

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                    walkDirection = walkDirection.add(new Vector3f(0.1f, 0, 0));
                }

                if (walkDirection.length() > 0) {
                    body.setWalkDirection(walkDirection);
                } else {
                    body.setWalkDirection(new Vector3f());
                }

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS && body.onGround()) {
                    body.jump();
                    lockOut = LOGIC_TICK_RATE / 4;
                }

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
                    body.setPhysicsLocation(new Vector3f(0, 10, 0));
                    body.setLinearVelocity(new Vector3f());
                    body.setAngularVelocity(new Vector3f());
                    lockOut = LOGIC_TICK_RATE / 4;
                }
            });
        }
    }
}