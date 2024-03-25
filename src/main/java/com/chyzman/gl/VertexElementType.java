package com.chyzman.gl;

import static org.lwjgl.opengl.GL45.GL_FLOAT;
import static org.lwjgl.opengl.GL45.GL_INT;

public enum VertexElementType {
    FLOAT(4, GL_FLOAT),
    INT(4, GL_INT);

    public final int size, glType;

    VertexElementType(int size, int glType) {
        this.size = size;
        this.glType = glType;
    }
}
