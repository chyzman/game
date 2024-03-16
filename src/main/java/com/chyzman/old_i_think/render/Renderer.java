package com.chyzman.old_i_think.render;

import com.chyzman.gl.GlProgram;
import com.chyzman.gl.GlShader;
import com.chyzman.old_i_think.Game;
import com.chyzman.old_i_think.Window;
import org.joml.Matrix4f;
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

    public void render(Mesh mesh) {
        Window window = Game.window;
        program.use();
        program.uniformMat4("projection", window.getProjectionMatrix());
        program.uniformMat4("view", window.getViewMatrix());
        program.uniformMat4("model", new Matrix4f());
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(mesh.getVaoID());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getTexture());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 12 * 3);
        GL30.glBindVertexArray(0);

        window.getViewMatrix().translate(3, 0, 0);

        program.uniformMat4("projection", window.getProjectionMatrix());
        program.uniformMat4("view", window.getViewMatrix());
        GL30.glBindVertexArray(mesh.getVaoID());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getTexture());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 12 * 3);
        GL30.glBindVertexArray(0);
    }
}