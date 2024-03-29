package com.chyzman.gl;

import static org.lwjgl.opengl.GL45.*;

public class GlVertexBuffer {

    private final int id = glGenBuffers();
    private int vboSize = 0;

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, this.id);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void upload(BufferWriter writer, boolean dynamic) {
        this.bind();

        if (writer.data.position() > this.vboSize) {
            glBufferData(GL_ARRAY_BUFFER, writer.data.slice(0, writer.data.position()), dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
            this.vboSize = writer.data.position();
        } else {
            glBufferSubData(GL_ARRAY_BUFFER, 0, writer.data.slice(0, writer.data.position()));
        }

        this.unbind();
    }

    public void delete() {
        glDeleteBuffers(this.id);
    }
}
