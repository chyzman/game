package com.chyzman.object;

import com.chyzman.Game;
import com.chyzman.component.position.Position;
import com.chyzman.gl.BufferWriter;
import com.chyzman.gl.ElementMeshBuffer;
import com.chyzman.gl.VertexDescriptors;
import com.chyzman.render.Renderer;
import com.chyzman.render.Texture;
import com.chyzman.util.Id;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class BasicObject {
    private final int chyz;
    private final ElementMeshBuffer<VertexDescriptors.PosColorTexVertexFunction> mesh;

    public BasicObject() {
        this.chyz = Texture.load(new Id("game", "grass_block.png"));

        this.mesh = new ElementMeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        mesh.builder.vertex(.5f, .5f, .0f, 1f, 1f, .0f, .0f, 1f, 1f); // top right
        mesh.builder.vertex(.5f, -.5f, .0f, 1f, .0f, 1f, .0f, 1f, .0f); // bottom right
        mesh.builder.vertex(-.5f, -.5f, .0f, 1f, .0f, .0f, 1f, .0f, .0f); // bottom left
        mesh.builder.vertex(-.5f, .5f, .0f, 1f, 1f, 1f, .0f, .0f, 1f);  // top left
        BufferWriter indices = mesh.getIndicesBuffer();
        indices.int3(0, 1, 3);
        indices.int3(3, 1, 2);
        mesh.upload(true);
    }

    public void draw(Position pos) {
        var renderer = Game.renderer;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, chyz);
        mesh.program.use();
        mesh.program.uniformMat4("projection", renderer.getProjectionMatrix().peek());
        mesh.program.uniformMat4("view", renderer.getViewMatrix().peek());


        mesh.program.uniformMat4("model", new Matrix4f(renderer.getModelMatrix().peek()).translate((float) pos.x, (float) pos.y, (float) pos.z));
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
