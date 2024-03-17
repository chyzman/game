package com.chyzman.object.components;

import com.chyzman.gl.BufferWriter;
import com.chyzman.gl.ElementMeshBuffer;
import com.chyzman.gl.VertexDescriptors;
import com.chyzman.object.GameObject;
import com.chyzman.render.Renderer;
import com.chyzman.render.Texture;
import com.chyzman.util.Id;

public class CoolCube extends GameObject {
    public final int chyz;
    public final ElementMeshBuffer<VertexDescriptors.PosColorTexVertexFunction> mesh;

    public CoolCube() {
        this.chyz = Texture.load(new Id("game", "chyz.png"));

        this.mesh = new ElementMeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        mesh.builder.vertex(.5f, .5f, 0f, 1f, 1f, 0f, 0f, 1f, 1f); // top right
        mesh.builder.vertex(.5f, -.5f, 0f, 1f, 0f, 1f, 0f, 1f, 0f); // bottom right
        mesh.builder.vertex(-.5f, -.5f, 0f, 1f, 0f, 0f, 1f, 0f, 0f); // bottom left
        mesh.builder.vertex(-.5f, .5f, 0f, 1f, 1f, 1f, 0f, 0f, 1f);  // top left
        BufferWriter indices = mesh.getIndicesBuffer();
        indices.int3(0, 1, 3);
        indices.int3(3, 1, 2);
        mesh.upload(true);
    }

    @Override
    public void update() {

    }
}
