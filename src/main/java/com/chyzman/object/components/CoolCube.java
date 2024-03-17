package com.chyzman.object.components;

import com.chyzman.Game;
import com.chyzman.gl.BufferWriter;
import com.chyzman.gl.ElementMeshBuffer;
import com.chyzman.gl.VertexDescriptors;
import com.chyzman.object.GameObject;
import com.chyzman.render.Renderer;
import com.chyzman.render.Texture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class CoolCube extends GameObject {
    private float x, y, z;
    private final int chyz;
    private final ElementMeshBuffer<VertexDescriptors.PosColorTexVertexFunction> mesh;

    public CoolCube() {
        this.chyz = Texture.loadTexture("grass_block.png");

        this.mesh = new ElementMeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEX, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        mesh.builder.vertex(0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f); // top right
        mesh.builder.vertex(0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f); // bottom right
        mesh.builder.vertex(-0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f); // bottom left
        mesh.builder.vertex(-0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f);  // top left
        BufferWriter indices = mesh.getIndicesBuffer();
        indices.int3(0, 1, 3);
        indices.int3(3, 1, 2);
        mesh.upload(true);
    }

    @Override
    public void update() {
        var window = Game.window;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, chyz);
        mesh.program.use();
        mesh.program.uniformMat4("projection", window.getProjectionMatrix().peek());
        mesh.program.uniformMat4("view", window.getViewMatrix().peek());


        mesh.program.uniformMat4("model", new Matrix4f(window.getModelMatrix().peek()).translate(x, y, z));
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
