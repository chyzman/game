package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.gl.*;
import com.chyzman.world.block.Blocks;
import com.chyzman.world.chunk.Chunk;
import com.chyzman.ui.core.Color;
import com.chyzman.util.Id;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class RenderChunk {
    private final MeshBuffer<VertexDescriptors.PosColorTexVertexFunction> mesh;
    private final int texture;
    private final Chunk chunk;
    private boolean compiled = false;

    public RenderChunk(Chunk chunk) {
        this.mesh = new MeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        this.chunk = chunk;
        this.texture = Texture.load(new Id("game", "test.png"));
    }

    public void compile() {
        mesh.clear();
        MatrixStack modelMatrix = Game.renderer.getModelMatrix();
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    if (Game.GAME.world.getBlock(x, y, z) == Blocks.GRASS) {
                        modelMatrix.push();
                        modelMatrix.translate(chunk.x + x, chunk.y + y, chunk.z + z);
                        //Bottom
                        quad(new Vector3d(0, 0, 0), new Vector3d(1, 0, 1), modelMatrix, Color.WHITE);
                        //Top
                        quad(new Vector3d(1, 1, 1), new Vector3d(0, 1, 0), modelMatrix, Color.WHITE);
                        //North
                        quad(new Vector3d(0, 0, 0), new Vector3d(1, 1, 0), modelMatrix, Color.WHITE);
                        //South
                        quad(new Vector3d(1, 0, 1), new Vector3d(0, 1, 1), modelMatrix, Color.WHITE);
                        //East
                        quad(new Vector3d(0, 0, 0), new Vector3d(0, 1, 1), modelMatrix, Color.WHITE);


//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f); // triangle 1 : begin
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f); // triangle 1 : end
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 1.0f, 1.0f); // triangle 2 : begin
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 0.0f, 0.0f); // triangle 2 : end
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 1.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 1.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 1.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 1.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 1.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 0.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), Color.RED, 1.0f, 0.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), Color.RED, 0.0f, 1.0f);
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), Color.RED, 0.0f, 0.0f);
                        modelMatrix.pop();
                    }
                }
            }
        }

        mesh.upload(true);
        compiled = true;
    }

    private void quad(Vector3d from, Vector3d to, MatrixStack matrices, Color color) {
        //Triangle 1
        mesh.builder.vertex(this.transform(matrices, (float) from.x, (float) from.y, (float) from.z), color, 1.0f, 1.0f);
        mesh.builder.vertex(this.transform(matrices, (float) from.x, (float) to.y, (float) to.z), color, 1.0f, 0.0f);
        mesh.builder.vertex(this.transform(matrices, (float) to.x, (float) to.y, (float) to.z), color, 0.0f, 0.0f);
        //Triangle 2
        mesh.builder.vertex(this.transform(matrices, (float) to.x, (float) to.y, (float) to.z), color, 0.0f, 0.0f);
        mesh.builder.vertex(this.transform(matrices, (float) to.x, (float) from.y, (float) from.z), color, 0.0f, 1.0f);
        mesh.builder.vertex(this.transform(matrices, (float) from.x, (float) from.y, (float) from.z), color, 1.0f, 1.0f);
    }

    private Vector3fc transform(MatrixStack matrices, float x, float y, float z) {
        var vec4 = new Vector4f(x, y, z, 1f).mul(matrices.peek());
        return new Vector3f(vec4.x, vec4.y, vec4.z);
    }

    public void draw() {
        var renderer = Game.renderer;
        mesh.program.use();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        if (!compiled || GLFW.glfwGetKey(Game.window.handle, GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS)
            System.out.println("Recompiling chunk");
            compile();
        MatrixStack modelMatrix = renderer.getModelMatrix();
        modelMatrix.push();
        modelMatrix.translate(chunk.x * Chunk.CHUNK_SIZE, chunk.y * Chunk.CHUNK_SIZE, chunk.z * Chunk.CHUNK_SIZE);
        mesh.program.uniformMat4("projection", renderer.getProjectionMatrix().peek());
        mesh.program.uniformMat4("view", renderer.getViewMatrix().peek());
        mesh.program.uniformMat4("model", modelMatrix.peek());
        modelMatrix.pop();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}