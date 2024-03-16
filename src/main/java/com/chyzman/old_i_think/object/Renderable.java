package com.chyzman.old_i_think.object;

import com.chyzman.old_i_think.Game;
import com.chyzman.old_i_think.render.Mesh;
import com.chyzman.old_i_think.render.MeshLoader;

public abstract class Renderable extends GameObject {
    protected Mesh mesh = MeshLoader.createMesh(
            new float[]{
                    -1.0f,-1.0f,-1.0f, // triangle 1 : begin
                    -1.0f,-1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f, // triangle 1 : end
                    1.0f, 1.0f,-1.0f, // triangle 2 : begin
                    -1.0f,-1.0f,-1.0f,
                    -1.0f, 1.0f,-1.0f, // triangle 2 : end
                    1.0f,-1.0f, 1.0f,
                    -1.0f,-1.0f,-1.0f,
                    1.0f,-1.0f,-1.0f,
                    1.0f, 1.0f,-1.0f,
                    1.0f,-1.0f,-1.0f,
                    -1.0f,-1.0f,-1.0f,
                    -1.0f,-1.0f,-1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f,-1.0f,
                    1.0f,-1.0f, 1.0f,
                    -1.0f,-1.0f, 1.0f,
                    -1.0f,-1.0f,-1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f,-1.0f, 1.0f,
                    1.0f,-1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f,-1.0f,-1.0f,
                    1.0f, 1.0f,-1.0f,
                    1.0f,-1.0f,-1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f,-1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f,-1.0f,
                    -1.0f, 1.0f,-1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f,-1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f,-1.0f, 1.0f
            },
            new int[]{
                    //Top
                    2, 6, 7,
                    2, 3, 7,

                    //Bottom
                    0, 4, 5,
                    0, 1, 5,

                    //Left
                    0, 2, 6,
                    0, 4, 6,

                    //Right
                    1, 3, 7,
                    1, 5, 7,

                    //Front
                    0, 2, 3,
                    0, 1, 3,

                    //Back
                    4, 6, 7,
                    4, 5, 7
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