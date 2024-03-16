package com.chyzman.gl;

import static org.lwjgl.opengl.GL45.GL_FLOAT;

public enum VertexElementType {
    FLOAT(4, GL_FLOAT);

    public final int size, glType;

    VertexElementType(int size, int glType) {
        this.size = size;
        this.glType = glType;
    }
}
