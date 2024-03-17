package com.chyzman.gl;

import static org.lwjgl.opengl.GL45.*;

public class GlElementArrayBuffer {

    private final int id = glGenBuffers();
    private int eboSize = 0;

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.id);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void upload(BufferWriter writer, boolean dynamic) {
        this.bind();

        if (writer.data.position() > this.eboSize) {
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, writer.data.slice(0, writer.data.position()), dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
            this.eboSize = writer.data.position();
        } else {
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, writer.data.slice(0, writer.data.position()));
        }

        this.unbind();
    }

    public void delete() {
        glDeleteBuffers(this.id);
    }
}
