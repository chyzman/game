package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.position.Velocity;
import com.chyzman.component.rotation.AngularVelocity;
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

import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.chyzman.Game.LOGGER;
import static com.chyzman.Game.LOGIC_TICK_RATE;

public class Physics {
    public static PhysicsSpace physicsSpace;

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
    }
}