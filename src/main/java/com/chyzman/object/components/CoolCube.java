package com.chyzman.object.components;

import com.chyzman.gl.GlProgram;
import com.chyzman.gl.GlShader;
import com.chyzman.gl.MeshBuffer;
import com.chyzman.gl.VertexDescriptors;
import com.chyzman.Game;
import com.chyzman.object.GameObject;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CoolCube extends GameObject {
    private final MeshBuffer<VertexDescriptors.PosVertexFunction> mesh;

    public CoolCube() {
        GlProgram program;
        try {
            program = new GlProgram(
                    "position",
                    GlShader.vertex(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.vert"))),
                    GlShader.fragment(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.frag")))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.mesh = new MeshBuffer<>(VertexDescriptors.POSITION, program);
        mesh.builder.vertex(0.0f,0.0f,0.0f); // triangle 1 : begin
        mesh.builder.vertex(0.0f,0.0f, 1.0f);
        mesh.builder.vertex(0.0f, 1.0f, 1.0f); // triangle 1 : end
        mesh.builder.vertex(1.0f, 1.0f,0.0f); // triangle 2 : begin
        mesh.builder.vertex(0.0f,0.0f,0.0f);
        mesh.builder.vertex(0.0f, 1.0f,0.0f); // triangle 2 : end
        mesh.builder.vertex(1.0f,0.0f, 1.0f);
        mesh.builder.vertex(0.0f,0.0f,0.0f);
        mesh.builder.vertex(1.0f,0.0f,0.0f);
        mesh.builder.vertex(1.0f, 1.0f,0.0f);
        mesh.builder.vertex(1.0f,0.0f,0.0f);
        mesh.builder.vertex(0.0f,0.0f,0.0f);
        mesh.builder.vertex(0.0f,0.0f,0.0f);
        mesh.builder.vertex(0.0f, 1.0f, 1.0f);
        mesh.builder.vertex(0.0f, 1.0f,0.0f);
        mesh.builder.vertex(1.0f,0.0f, 1.0f);
        mesh.builder.vertex(0.0f,0.0f, 1.0f);
        mesh.builder.vertex(0.0f,0.0f,0.0f);
        mesh.builder.vertex(0.0f, 1.0f, 1.0f);
        mesh.builder.vertex(0.0f,0.0f, 1.0f);
        mesh.builder.vertex(1.0f,0.0f, 1.0f);
        mesh.builder.vertex(1.0f, 1.0f, 1.0f);
        mesh.builder.vertex(1.0f,0.0f,0.0f);
        mesh.builder.vertex(1.0f, 1.0f,0.0f);
        mesh.builder.vertex(1.0f,0.0f,0.0f);
        mesh.builder.vertex(1.0f, 1.0f, 1.0f);
        mesh.builder.vertex(1.0f,0.0f, 1.0f);
        mesh.builder.vertex(1.0f, 1.0f, 1.0f);
        mesh.builder.vertex(1.0f, 1.0f,0.0f);
        mesh.builder.vertex(0.0f, 1.0f,0.0f);
        mesh.builder.vertex(1.0f, 1.0f, 1.0f);
        mesh.builder.vertex(0.0f, 1.0f,0.0f);
        mesh.builder.vertex(0.0f, 1.0f, 1.0f);
        mesh.builder.vertex(1.0f, 1.0f, 1.0f);
        mesh.builder.vertex(0.0f, 1.0f, 1.0f);
        mesh.builder.vertex(1.0f,0.0f, 1.0f);
        mesh.upload(false);
    }

    @Override
    public void update() {
        var window = Game.window;

        mesh.program.use();
        mesh.program.uniformMat4("projection", window.getProjectionMatrix());
        mesh.program.uniformMat4("view", window.getViewMatrix());
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    mesh.program.uniformMat4("model", new Matrix4f(window.getModelMatrix()).translate(x, y, z));
                    mesh.draw();
                }
            }
        }

    }
}