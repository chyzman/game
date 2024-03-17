package com.chyzman.gl;

import com.chyzman.ui.core.Color;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL45.*;

public class ImmediatePrimitiveRenderer {

    private final RenderContext context;

    public final MeshBuffer<VertexDescriptors.PosColorVertexFunction> posColorBuffer;

    public ImmediatePrimitiveRenderer(RenderContext context) {
        this.context = context;
        this.posColorBuffer = new MeshBuffer<>(VertexDescriptors.POSITION_COLOR, context.findProgram("gui_pos_color"));
    }

    public void rect(float x, float y, float width, float height, Color color, Matrix4fc projection) {
        this.posColorBuffer.program.use();
        this.posColorBuffer.program.uniformMat4("uProjection", projection);

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
