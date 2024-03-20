package com.chyzman.systems;

import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.position.Velocity;
import com.chyzman.component.rotation.AngularVelocity;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.IdentifiedSystem;
import com.chyzman.util.Id;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.chyzman.Game.LOGIC_TICK_RATE;

public class Physics {
    public static final double TERMINAL_VELOCITY = 100d/(double) LOGIC_TICK_RATE;

    public static IdentifiedSystem<FramedDominion> create(FramedDominion dominion, Supplier<Double> deltaTime){
        return IdentifiedSystem.of(new Id("game", "physics"), dominion, deltaTime, Physics::update);
    }

    public static void update(FramedDominion dom, double deltaTime) {
        // update velocity of entities with gravity
        dom.findEntitiesWith(Velocity.class, Gravity.class).forAll((entity, velocity, gravity) -> {
            velocity.add(gravity);
            if (velocity.lengthSquared() > Math.pow(TERMINAL_VELOCITY, 2)) {
                velocity.normalize().mul(TERMINAL_VELOCITY);
            }
        });

        // update rotation of entities with angular velocity
        dom.findEntitiesWith(Rotation.class, AngularVelocity.class).forAll((entity, rotation, angularVelocity) -> {
            rotation.add(angularVelocity);
        });

        // update position of entities with velocity
        dom.findEntitiesWith(Position.class, Velocity.class).forAll((entity, position, velocity) -> {
            position.add(velocity);
            //            System.out.printf("\r%s: Pos:(%s, %s, %s) Vel:(%s,%s,%s)", result.entity().getName(), pos.x, pos.y, pos.z, velocity.x*(double)LOGIC_TICK_RATE, velocity.y*(double)LOGIC_TICK_RATE, velocity.z*(double)LOGIC_TICK_RATE);
        });

        dom.findEntitiesWith(Position.class).comp1Iterable().forEach(pos -> {
            if (pos.queue != null) {
                pos.set(pos.queue.stream().reduce(Function.identity(), Function::andThen).apply(pos));
                pos.queue.clear();
            }
        });
    }
}