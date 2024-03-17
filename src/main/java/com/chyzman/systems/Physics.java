package com.chyzman.systems;

import com.chyzman.component.Rotation.AngularVelocity;
import com.chyzman.component.Rotation.Rotation;
import com.chyzman.component.position.Floatly;
import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.position.Velocity;
import dev.dominion.ecs.api.Dominion;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.chyzman.Game.LOGIC_TICK_RATE;

public class Physics {
    public static final double TERMINAL_VELOCITY = 100d/(double) LOGIC_TICK_RATE;

    public static DomSystem create(Dominion dominion, Supplier<Double> deltaTime){
        return DomSystem.create(dominion, "physics", deltaTime, Physics::update);
    }

    public static void update(Dominion dom, double deltaTime) {
        // update velocity of entities with gravity
        dom.findEntitiesWith(Velocity.class, Gravity.class).forEach(result -> {
            Velocity velocity = result.comp1();
            Gravity gravity = result.comp2();
            velocity.add(gravity);
            if (velocity.lengthSquared() > Math.pow(TERMINAL_VELOCITY, 2)) {
                velocity.normalize().mul(TERMINAL_VELOCITY);
            }
        });

        // update rotation of entities with angular velocity
        dom.findEntitiesWith(Rotation.class, AngularVelocity.class).forEach(result -> {
            Rotation rotation = result.comp1();
            AngularVelocity angularVelocity = result.comp2();
            rotation.add(angularVelocity);
        });

        // update position of entities with velocity
        dom.findEntitiesWith(Position.class, Velocity.class).forEach(result -> {
            Position pos = result.comp1();
            Velocity velocity = result.comp2();
            pos.add(velocity);
//            System.out.printf("\r%s: Pos:(%s, %s, %s) Vel:(%s,%s,%s)", result.entity().getName(), pos.x, pos.y, pos.z, velocity.x*(double)LOGIC_TICK_RATE, velocity.y*(double)LOGIC_TICK_RATE, velocity.z*(double)LOGIC_TICK_RATE);
        });

        dom.findEntitiesWith(Position.class).forEach(result -> {
            Position pos = result.comp();
            if (pos.queue != null) {
                pos.set(pos.queue.stream().reduce(Function.identity(), Function::andThen).apply(pos));
                pos.queue.clear();
            }
        });
    }
}