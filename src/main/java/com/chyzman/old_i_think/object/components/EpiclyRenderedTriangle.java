package com.chyzman.old_i_think.object.components;

import com.chyzman.gl.GlProgram;
import com.chyzman.gl.GlShader;
import com.chyzman.gl.MeshBuffer;
import com.chyzman.gl.VertexDescriptors;
import com.chyzman.old_i_think.Game;
import com.chyzman.old_i_think.object.GameObject;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EpiclyRenderedTriangle extends GameObject {

    private final MeshBuffer<VertexDescriptors.PosVertexFunction> mesh;

    public EpiclyRenderedTriangle() {
        GlProgram program;
        try {
            program = new GlProgram(
                    "position",
                    GlShader.vertex(Files.newInputStream(Path.of("src/main/resources/shaders/position.vert"))),
                    GlShader.fragment(Files.newInputStream(Path.of("src/main/resources/shaders/position.frag")))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.mesh = new MeshBuffer<>(VertexDescriptors.POSITION, program);
        mesh.builder.vertex(0, 0, 0);
        mesh.builder.vertex(0, 100, 0);
        mesh.builder.vertex(100, 0, 0);
        mesh.upload(false);
    }

    @Override
    public void update() {
        var window = Game.window;

        mesh.program.use();
        mesh.program.uniformMat4("projection", new Matrix4f().setOrtho(0, window.width, window.height, 0, 0, 1000));
        mesh.draw();
    }
}
