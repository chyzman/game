package com.chyzman;

import com.chyzman.component.Rotation.AngularVelocity;
import com.chyzman.component.Rotation.Rotation;
import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.position.Velocity;
import dev.dominion.ecs.api.Dominion;
import com.chyzman.component.*;
import dev.dominion.ecs.api.Scheduler;

public class BadGame {
    public static final int TICK_RATE = 3; //will be 64 in the real game but is 3 so u can see shit happening
    public static final double TERMINAL_VELOCITY = 100d/(double)TICK_RATE;

    public static void main(String[] args) {
        Dominion game = Dominion.create();

        game.createEntity(
                "player",
                new Position(0, 0, 0),
                new Velocity(),
                new Gravity(0, -9.8/TICK_RATE, 0),
                new Rotation(),
                new AngularVelocity(),
                new Health(100, 100)
        );

        Runnable system = () -> {
            game.findEntitiesWith(Velocity.class, Gravity.class).forEach(result -> {
                Velocity velocity = result.comp1();
                Gravity gravity = result.comp2();
                velocity.add(gravity);
                if (velocity.lengthSquared() > Math.pow(TERMINAL_VELOCITY, 2)) {
                    velocity.normalize().mul(TERMINAL_VELOCITY);
                }
            });

            game.findEntitiesWith(Position.class, Velocity.class).forEach(result -> {
                Position pos = result.comp1();
                Velocity velocity = result.comp2();
                pos.add(velocity);
                System.out.printf("\r%s: Pos:(%s, %s, %s) Vel:(%s,%s,%s)", result.entity().getName(), pos.x, pos.y, pos.z, velocity.x*(double)TICK_RATE, velocity.y*(double)TICK_RATE, velocity.z*(double)TICK_RATE);
            });
        };

        Scheduler scheduler = game.createScheduler();

        scheduler.schedule(system);

        scheduler.tickAtFixedRate(TICK_RATE);

    }
}