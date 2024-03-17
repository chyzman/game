package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.gl.*;
import com.chyzman.systems.Chunk;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class RenderChunk {
    private final MeshBuffer<VertexDescriptors.PosColorTexVertexFunction> mesh;
    private final int texture;
    private final Chunk chunk;
    private boolean compiled = false;

    public RenderChunk(Chunk chunk) {
        this.mesh = new MeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEX, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        this.chunk = chunk;
        this.texture = Texture.loadTexture("grass_block.png");

    }

    public void compile() {
        mesh.clear();
        MatrixStack modelMatrix = Game.window.getModelMatrix();
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    if (chunk.getBlock(x, y, z)) {
                        modelMatrix.push();
                        modelMatrix.translate(chunk.x + x, chunk.y + y, chunk.z + z);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f); // triangle 1 : begin
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f); // triangle 1 : end
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 1.0f); // triangle 2 : begin
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f); // triangle 2 : end
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f,0.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
                        mesh.builder.vertex(modelMatrix.peek().transform(new Vector4f(1.0f,0.0f, 1.0f, 1.0f)), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                        modelMatrix.pop();
                    }
                }
            }
        }

        mesh.upload(true);
        compiled = true;
    }

    public void draw() {
        var window = Game.window;
        mesh.program.use();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        if (!compiled)
            compile();
        mesh.program.uniformMat4("projection", window.getProjectionMatrix().peek());
        mesh.program.uniformMat4("view", window.getViewMatrix().peek());
        mesh.program.uniformMat4("model", window.getModelMatrix().peek());
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
