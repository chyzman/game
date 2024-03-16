package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.position.Velocity;
import dev.dominion.ecs.api.Dominion;

import static com.chyzman.Game.LOGIC_TICK_RATE;

public class Physics {
    public static final double TERMINAL_VELOCITY = 100d/(double) LOGIC_TICK_RATE;

    public static void update(Dominion dom) {
        // update velocity of things with gravity
        dom.findEntitiesWith(Velocity.class, Gravity.class).forEach(result -> {
            Velocity velocity = result.comp1();
            Gravity gravity = result.comp2();
            velocity.add(gravity);
            if (velocity.lengthSquared() > Math.pow(TERMINAL_VELOCITY, 2)) {
                velocity.normalize().mul(TERMINAL_VELOCITY);
            }
        });

        // update position of things with velocity
        dom.findEntitiesWith(Position.class, Velocity.class).forEach(result -> {
            Position pos = result.comp1();
            Velocity velocity = result.comp2();
            pos.add(velocity);
//            System.out.printf("\r%s: Pos:(%s, %s, %s) Vel:(%s,%s,%s)", result.entity().getName(), pos.x, pos.y, pos.z, velocity.x*(double)LOGIC_TICK_RATE, velocity.y*(double)LOGIC_TICK_RATE, velocity.z*(double)LOGIC_TICK_RATE);
        });
    }
}