package com.chyzman.object;

import com.chyzman.Game;
import com.chyzman.render.Mesh;
import com.chyzman.render.MeshLoader;

public abstract class Renderable extends GameObject {
    protected Mesh mesh = MeshLoader.createMesh(
            new float[]{
                    -10f, -10f, 0,
                    10f, -10f, 0,
                    -10f, 10f, 0,
                    10f, 10f, 0
            },
            new int[]{
                    0,1,2,
                    1,2,3
            },
            new float[]{
                    0,1,
                    1,1,
                    0,0,
                    1,0
            }
    );

    public void draw() {
        Game.renderer.render(mesh);
    }

    @Override
    public void update() {
        draw();
    }
}