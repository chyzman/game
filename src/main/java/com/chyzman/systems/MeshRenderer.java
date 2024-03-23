package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.component.position.Position;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.IdentifiedSystem;
import com.chyzman.gl.MatrixStack;
import com.chyzman.object.components.MeshComponent;
import com.chyzman.util.Id;

import java.util.function.Supplier;

public class MeshRenderer {
    public static IdentifiedSystem<FramedDominion> create(FramedDominion dominion, Supplier<Double> deltaTime){
        return IdentifiedSystem.of(new Id("game", "mesh_renderer"), dominion, deltaTime, MeshRenderer::update);
    }

    public static void update(FramedDominion dom, double deltaTime) {


    }
}
