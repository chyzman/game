package com.chyzman.systems;

import com.chyzman.Game;
import com.chyzman.gl.GlProgram;
import com.chyzman.gl.MeshBuffer;
import com.chyzman.gl.RenderContext;
import com.chyzman.gl.VertexDescriptors;
import com.chyzman.render.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class TextRenderer {
    private final MeshBuffer<VertexDescriptors.TextVertexFunction> mesh;
    private final Font font = new Font("FiraCode-VariableFont_wght.ttf");

    public TextRenderer(RenderContext context) {
        mesh = new MeshBuffer<>(VertexDescriptors.FONT, context.findProgram("font"));
    }

    public void renderText(String text, float x, float y, float scale, Vector3f color) {
        // activate corresponding render state
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        mesh.program.use();
        mesh.program.uniformMat4("uProjection", new Matrix4f().setOrtho2D(0.0f, Game.window.width, 0.0f, Game.window.height));
        mesh.program.uniform3f("uTextColor", color.x, color.y, color.z);
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