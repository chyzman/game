package com.chyzman.registry;

import com.chyzman.component.Named;
import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.position.Velocity;
import com.chyzman.util.Id;

import java.util.UUID;

public class Frameworks {

    public static final Framework POSITIONED_ENTITY = new Frame(gameId("positioned"))
            .addComponent(Position.class, Position::zeroPos)
            .build();

    public static final Framework PHYSICS_ENTITY = new Frame(gameId("physics"))
            .addComponent(Position.class, Position::zeroPos)
            .addComponent(Velocity.class, Velocity::zeroVel)
            .addComponent(Gravity.class, Gravity::zeroGrav)
            .build();

    public static final Framework UNIQUE_ENTITY = PHYSICS_ENTITY.framed(gameId("unique_entity"))
            .addComponent(UUID.class, UUID::randomUUID)
            .addComponent(Named.class, Named::empty)
            .build();

    public static Id gameId(String path) {
        return new Id("game", path);
    }
}
