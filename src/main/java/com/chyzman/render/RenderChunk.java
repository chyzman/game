package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.gl.*;
import com.chyzman.util.Direction;
import com.chyzman.util.Vec3i;
import com.chyzman.world.block.Block;
import com.chyzman.world.block.Blocks;
import com.chyzman.world.chunk.Chunk;
import com.chyzman.ui.core.Color;
import com.chyzman.util.Id;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenderChunk {
    private static final Obj CUBE;

    static {
        try {
            CUBE = ObjUtils.convertToRenderable(ObjReader.read(Files.newInputStream(Path.of("src/main/resources/models/cube.obj"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final ElementMeshBuffer<VertexDescriptors.PosColorTexNormalVertexFunction> mesh;
    private final int texture;
    private final Chunk chunk;
    private boolean compiled = false;

    public RenderChunk(Chunk chunk) {
        this.mesh = new ElementMeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE_NORMAL, Renderer.POS_COLOR_TEXTURE_NORMAL_PROGRAM);
        this.chunk = chunk;
        this.texture = Texture.load(new Id("game", "test.png"));
    }

    public void compile() {
        mesh.clear();
        MatrixStack modelMatrix = Game.renderer.getModelMatrix();
        int cubes = 0;
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    Block block = Game.GAME.world.getBlock(chunk.x * Chunk.CHUNK_SIZE + x, chunk.y * Chunk.CHUNK_SIZE + y, chunk.z * Chunk.CHUNK_SIZE + z);
                    if (block != null) {
                        modelMatrix.push();
                        modelMatrix.translate(chunk.x + x, chunk.y + y, chunk.z + z);
//                        //Bottom
//                        quad(new Vector3d(0, 0, 0), new Vector3d(1, 0, 1), modelMatrix, block.color, Direction.DOWN);
//                        //Top
//                        quad(new Vector3d(1, 1, 1), new Vector3d(0, 1, 0), modelMatrix, block.color, Direction.UP);
//                        //North
//                        quad(new Vector3d(0, 0, 0), new Vector3d(1, 1, 0), modelMatrix, block.color, Direction.NORTH);
//                        //South
//                        quad(new Vector3d(1, 0, 1), new Vector3d(0, 1, 1), modelMatrix, block.color, Direction.SOUTH);
//                        //East
//                        quad(new Vector3d(0, 0, 0), new Vector3d(0, 1, 1), modelMatrix, block.color, Direction.EAST);
//                        //West
//                        quad(new Vector3d(1, 0, 0), new Vector3d(1, 1, 1), modelMatrix, block.color, Direction.WEST);


                        int[] indices = ObjData.getFaceVertexIndicesArray(CUBE);
                        float[] vertices = ObjData.getVerticesArray(CUBE);
                        float[] texCoords = ObjData.getTexCoordsArray(CUBE, 2);
                        float[] normals = ObjData.getNormalsArray(CUBE);

                        for (int i = 0; i < vertices.length; i += 3) {
                            mesh.builder.vertex(transform(modelMatrix, vertices[i], vertices[i + 1], vertices[i + 2]), block.color, texCoords[i / 3], texCoords[i / 3 + 1], new Vector3f(normals[i], normals[i + 1], normals[i + 2]));
                        }

                        BufferWriter in = mesh.getIndicesBuffer();

                        for (int i = 0; i < indices.length; i += 3) {
                            in.int3((cubes * vertices.length) + indices[i], (cubes * vertices.length) + indices[i + 1], (cubes * vertices.length) + indices[i + 2]);
                        }

//                        quad(modelMatrix, Direction.WEST, block.color, -1.0f,-1.0f,-1.0f, -1.0f,-1.0f, 1.0f, -1.0f, 1.0f, 1.0f);  // Left Side
//                        quad(modelMatrix, Direction.WEST, block.color, -1.0f,-1.0f,-1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f,-1.0f);  // Left Side
//                        quad(modelMatrix, Direction.NORTH, block.color, 1.0f, 1.0f,-1.0f, -1.0f,-1.0f,-1.0f, -1.0f, 1.0f,-1.0f);  // Back Side
//                        quad(modelMatrix, Direction.DOWN, block.color, 1.0f,-1.0f, 1.0f, -1.0f,-1.0f,-1.0f,  1.0f,-1.0f,-1.0f);  // Bottom Side
//                        quad(modelMatrix, Direction.NORTH, block.color, 1.0f, 1.0f,-1.0f,  1.0f,-1.0f,-1.0f, -1.0f,-1.0f,-1.0f);  // Back Side
//                        quad(modelMatrix, Direction.DOWN, block.color, 1.0f,-1.0f, 1.0f, -1.0f,-1.0f, 1.0f, -1.0f,-1.0f,-1.0f);  // Bottom Side
//                        quad(modelMatrix, Direction.SOUTH, block.color, -1.0f, 1.0f, 1.0f, -1.0f,-1.0f, 1.0f,  1.0f,-1.0f, 1.0f);  // Front Side
//                        quad(modelMatrix, Direction.EAST, block.color, 1.0f, 1.0f, 1.0f,  1.0f,-1.0f,-1.0f,  1.0f, 1.0f,-1.0f);  // Right Side
//                        quad(modelMatrix, Direction.EAST, block.color, 1.0f,-1.0f,-1.0f,  1.0f, 1.0f, 1.0f,  1.0f,-1.0f, 1.0f);  // Right Side
//                        quad(modelMatrix, Direction.UP, block.color, 1.0f, 1.0f, 1.0f,  1.0f, 1.0f,-1.0f, -1.0f, 1.0f,-1.0f);  // Top Side
//                        quad(modelMatrix, Direction.UP, block.color, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f,-1.0f, -1.0f, 1.0f, 1.0f);  // Top Side
//                        quad(modelMatrix, Direction.SOUTH, block.color, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f,  1.0f,-1.0f, 1.0f);   // Front Side


//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), block.color, 0.0f, 1.0f, Direction.SOUTH.normal()); // triangle 1 : begin
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), block.color, 0.0f, 0.0f, Direction.SOUTH.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), block.color, 1.0f, 0.0f, Direction.SOUTH.normal()); // triangle 1 : end
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), block.color, 1.0f, 1.0f, Direction.NORTH.normal()); // triangle 2 : begin
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), block.color, 0.0f, 0.0f, Direction.UP.normal()); // triangle 2 : end
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), block.color, 1.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), block.color, 1.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), block.color, 0.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), block.color, 1.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), block.color, 0.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), block.color, 1.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), block.color, 1.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), block.color, 0.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f,0.0f), block.color, 1.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), block.color, 1.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f,0.0f, 1.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), block.color, 0.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), block.color, 1.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), block.color, 1.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f,0.0f), block.color, 0.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), block.color, 1.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), block.color, 1.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f,0.0f), block.color, 0.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), block.color, 1.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), block.color, 1.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f,0.0f), block.color, 0.0f, 1.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), block.color, 0.0f, 0.0f, Direction.UP.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f, 1.0f, 1.0f), block.color, 1.0f, 0.0f, Direction.EAST.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 0.0f, 1.0f, 1.0f), block.color, 0.0f, 1.0f, Direction.EAST.normal());
//                        mesh.builder.vertex(this.transform(modelMatrix, 1.0f,0.0f, 1.0f), block.color, 0.0f, 0.0f, Direction.EAST.normal());
                        modelMatrix.pop();
                        cubes++;
                    }
                }
            }
        }

        mesh.upload(true);
        compiled = true;
    }

    private void quad(MatrixStack matrices, Direction normal, Color color, float ...vertices) {
//        mesh.builder.vertex(transform(matrices, vertices[0], vertices[1], vertices[2]), color, 1, 1, normal.normal());
//        mesh.builder.vertex(transform(matrices, vertices[3], vertices[4], vertices[5]), color, 1, 1, normal.normal());
//        mesh.builder.vertex(transform(matrices, vertices[6], vertices[7], vertices[8]), color, 1, 1, normal.normal());
    }

    private void quad(Vector3d from, Vector3d to, MatrixStack matrices, Color color, Direction normal) {
        //Triangle 1
//        mesh.builder.vertex(this.transform(matrices, (float) from.x, (float) from.y, (float) from.z), color, 1.0f, 1.0f, normal.normal());
//        mesh.builder.vertex(this.transform(matrices, (float) from.x, (float) to.y, (float) to.z), color, 1.0f, 0.0f, normal.normal());
//        mesh.builder.vertex(this.transform(matrices, (float) to.x, (float) to.y, (float) to.z), color, 0.0f, 0.0f, normal.normal());
//        //Triangle 2
//        mesh.builder.vertex(this.transform(matrices, (float) to.x, (float) to.y, (float) to.z), color, 0.0f, 0.0f, normal.normal());
//        mesh.builder.vertex(this.transform(matrices, (float) to.x, (float) from.y, (float) from.z), color, 0.0f, 1.0f, normal.normal());
//        mesh.builder.vertex(this.transform(matrices, (float) from.x, (float) from.y, (float) from.z), color, 1.0f, 1.0f, normal.normal());
    }

    private Vector3fc transform(MatrixStack matrices, float x, float y, float z) {
        var vec4 = new Vector4f(x, y, z, 1f).mul(matrices.peek());
        return new Vector3f(vec4.x, vec4.y, vec4.z);
    }

    public void draw() {
        var renderer = Game.renderer;
        mesh.program.use();
        if (!compiled || GLFW.glfwGetKey(Game.window.handle, GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS) {
            System.out.println("Recompiling chunk");
            compile();
        }
        MatrixStack modelMatrix = renderer.getModelMatrix();
        modelMatrix.push();
        modelMatrix.translate(chunk.x * Chunk.CHUNK_SIZE, chunk.y * Chunk.CHUNK_SIZE, chunk.z * Chunk.CHUNK_SIZE);
        mesh.program.uniformMat4("uProjection", renderer.getProjectionMatrix().peek());
        mesh.program.uniformMat4("uView", renderer.getViewMatrix().peek());
        mesh.program.uniformMat4("uModel", modelMatrix.peek());
        mesh.program.uniformSampler("uTexture", texture, 0);
        modelMatrix.pop();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}