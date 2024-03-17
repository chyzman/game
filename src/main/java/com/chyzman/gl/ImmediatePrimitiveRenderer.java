package com.chyzman.gl;

import com.chyzman.ui.core.Color;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL45.*;

public class ImmediatePrimitiveRenderer {

    private final RenderContext context;

    public final MeshBuffer<VertexDescriptors.PosColorVertexFunction> posColorBuffer;
    public final MeshBuffer<VertexDescriptors.PosColorTexVertexFunction> posColorTexBuffer;

    public ImmediatePrimitiveRenderer(RenderContext context) {
        this.context = context;
        this.posColorBuffer = new MeshBuffer<>(VertexDescriptors.POSITION_COLOR, context.findProgram("gui_pos_color"));
        this.posColorTexBuffer = new MeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE, context.findProgram("gui_pos_color_tex"));
    }

    public void texturedRect(int textureName, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, int textureWidth, int textureHeight, Color color, Matrix4fc projection) {
        this.posColorTexBuffer.program.use();
        this.posColorTexBuffer.program.uniformMat4("uProjection", projection);
        this.posColorTexBuffer.program.uniformSampler("uTexture", textureName, 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float u0 = u / textureWidth, v0 = 1 - v / textureHeight;
        float u1 = u0 + regionWidth / textureWidth, v1 = v0 - regionHeight / textureHeight;

        this.posColorTexBuffer.clear();
        this.posColorTexBuffer.builder.vertex(new Vector3f(x, y, 0), color, u0, v0);
        this.posColorTexBuffer.builder.vertex(new Vector3f(x, y + height, 0), color, u0, v1);
        this.posColorTexBuffer.builder.vertex(new Vector3f(x + width, y + height, 0), color, u1, v1);
        this.posColorTexBuffer.builder.vertex(new Vector3f(x + width, y + height, 0), color, u1, v1);
        this.posColorTexBuffer.builder.vertex(new Vector3f(x + width, y, 0), color, u1, v0);
        this.posColorTexBuffer.builder.vertex(new Vector3f(x, y, 0), color, u0, v0);
        this.posColorTexBuffer.upload(true);
        this.posColorTexBuffer.draw();
    }

    public void rect(float x, float y, float width, float height, Color color, Matrix4fc projection) {
        this.posColorBuffer.program.use();
        this.posColorBuffer.program.uniformMat4("uProjection", projection);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.posColorBuffer.clear();
        this.buildRect(this.posColorBuffer.builder, x, y, width, height, color);
        this.posColorBuffer.upload(true);
        this.posColorBuffer.draw();
    }

    protected void buildRect(VertexDescriptors.PosColorVertexFunction builder, float x, float y, float width, float height, Color color) {
        builder.vertex(new Vector3f(x, y, 0), color);
        builder.vertex(new Vector3f(x, y + height, 0), color);
        builder.vertex(new Vector3f(x + width, y + height, 0), color);
        builder.vertex(new Vector3f(x + width, y + height, 0), color);
        builder.vertex(new Vector3f(x + width, y, 0), color);
        builder.vertex(new Vector3f(x, y, 0), color);
    }
}
