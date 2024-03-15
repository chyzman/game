package com.chyzman.old_i_think.render;

import com.chyzman.old_i_think.Game;
import com.chyzman.old_i_think.render.shader.ShaderTextured;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public class Renderer {

    ShaderTextured shader = new ShaderTextured();

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Mesh mesh) {
        shader.start();
        shader.setProjection(Game.window.getProjectionMatrix());
        GL30.glBindVertexArray(mesh.getVaoID());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getTexture());
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT,0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
}