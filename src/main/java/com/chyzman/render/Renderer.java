package com.chyzman.render;

import com.chyzman.gl.GlProgram;
import com.chyzman.gl.GlShader;
import com.chyzman.Game;
import com.chyzman.Window;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Renderer {

    GlProgram program = new GlProgram("main", GlShader.vertex(shader("Textured.vert")), GlShader.fragment(shader("Textured.frag")));

    private static InputStream shader(String shader) {
        try {
            return Files.newInputStream(Path.of("src/main/resources/shaders/" + shader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
    }
}