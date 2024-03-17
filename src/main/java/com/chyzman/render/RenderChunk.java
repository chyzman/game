package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.gl.*;
import com.chyzman.systems.Chunk;
import com.chyzman.ui.core.Color;
import com.chyzman.util.Id;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class RenderChunk {
    private final MeshBuffer<VertexDescriptors.PosColorTexVertexFunction> mesh;
    private final int texture;
    private final Chunk chunk;
    private boolean compiled = false;

    public RenderChunk(Chunk chunk) {
        this.mesh = new MeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        this.chunk = chunk;
        this.texture = Texture.load(new Id("game", "grass_block.png"));
    }

    public void compile() {
        mesh.clear();
        MatrixStack modelMatrix = Game.renderer.getModelMatrix();
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    if (chunk.getBlock(x, y, z)) {
                        modelMatrix.push();
                        modelMatrix.translate(chunk.x + x, chunk.y + y, chunk.z + z);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f); // triangle 1 : begin
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f); // triangle 1 : end
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 1.0f, 1.0f); // triangle 2 : begin
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 0.0f, 0.0f); // triangle 2 : end
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 1.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 1.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 1.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 1.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 1.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 0.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 0.0f, 1.0f);
                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
                        modelMatrix.pop();
                    }
                }
            }
        }

        mesh.upload(true);
        compiled = true;
    }

    private Vector3fc transform(MatrixStack matrices, float x, float y, float z) {
        var vec4 = new Vector4f(x, y, z, 1f).mul(matrices.peek());
        return new Vector3f(vec4.x, vec4.y, vec4.z);
    }

    public void draw() {
        var renderer = Game.renderer;
        mesh.program.use();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        if (!compiled)
            compile();
        mesh.program.uniformMat4("projection", renderer.getProjectionMatrix().peek());
        mesh.program.uniformMat4("view", renderer.getViewMatrix().peek());
        mesh.program.uniformMat4("model", renderer.getModelMatrix().peek());
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
