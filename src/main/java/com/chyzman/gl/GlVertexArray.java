package com.chyzman.gl;

import static org.lwjgl.opengl.GL45.*;

public class GlVertexArray {

    private final int id = glGenVertexArrays();

    public void draw(int count) {
        this.bind();
        glDrawArrays(GL_TRIANGLES, 0, count);
        this.unbind();
    }

    public void bind() {
        glBindVertexArray(this.id);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void delete() {
        glDeleteVertexArrays(this.id);
    }
}
