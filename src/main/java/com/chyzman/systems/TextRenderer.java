package com.chyzman.systems;

import com.chyzman.gl.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;

public class TextRenderer {
    private final MeshBuffer<VertexDescriptors.TextVertexFunction> mesh;
    private final Font font = new Font("arial.ttf");
    private final GlProgram defaultProgram;

    public TextRenderer() {
        try {
            this.defaultProgram = new GlProgram(
                    "font",
                    GlShader.vertex("font.vert"),
                    GlShader.fragment("font.frag")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mesh = new MeshBuffer<>(VertexDescriptors.FONT, defaultProgram);
    }

    public void renderText(String text, float x, float y, float scale, Vector3f color) {
        renderText(defaultProgram, text, x, y, scale, color);
    }

    void renderText(GlProgram program, String text, float x, float y, float scale, Vector3f color) {
        // activate corresponding render state
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        program.use();
        program.uniformMat4("projection", new Matrix4f().setOrtho2D(0.0f, 800.0f, 0.0f, 600.0f));
        program.uniform3f("textColor", color.x, color.y, color.z);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);

        // iterate through all characters

        for (char c : text.toCharArray()) {
            Font.FontCharacter ch = font.characters.get(c);

            float xpos = x + ch.bearing().x * scale;
            float ypos = y - (ch.size().y - ch.bearing().y) * scale;

            float w = ch.size().x * scale;
            float h = ch.size().y * scale;
            // update VBO for each character
            mesh.builder.vertex(xpos, ypos + h, 0.0f, 0.0f);
            mesh.builder.vertex(xpos, ypos, 0.0f, 1.0f);
            mesh.builder.vertex(xpos + w, ypos, 1.0f, 1.0f);

            mesh.builder.vertex(xpos, ypos + h, 0.0f, 0.0f);
            mesh.builder.vertex(xpos + w, ypos, 1.0f, 1.0f);
            mesh.builder.vertex(xpos + w, ypos + h, 1.0f, 0.0f);
            // render glyph texture over quad
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, ch.textureId());
            // update content of VBO memory
            mesh.upload(true);
            // render quad
            mesh.draw();
            mesh.clear();
            // now advance cursors for next glyph (note that advance is number of 1/64 pixels)
            x += (ch.advance() >> 6) * scale; // bitshift by 6 to get value in pixels (2^6 = 64)
        }
//        GL30.glBindVertexArray(0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
    }
}
