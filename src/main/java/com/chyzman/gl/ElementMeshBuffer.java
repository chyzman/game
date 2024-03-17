package com.chyzman.gl;

import static org.lwjgl.opengl.GL11.*;

public class ElementMeshBuffer<VF> {
    private final GlVertexBuffer vbo = new GlVertexBuffer();
    private final GlVertexArray vao = new GlVertexArray();
    private final GlElementArrayBuffer ebo = new GlElementArrayBuffer();

    private final BufferWriter buffer;
    private final BufferWriter indicesBuffer;
    private final VertexDescriptor<VF> descriptor;

    public final GlProgram program;
    public final VF builder;

    public ElementMeshBuffer(VertexDescriptor<VF> descriptor, GlProgram program) {
        this(descriptor, program, 1024);
    }

    public ElementMeshBuffer(VertexDescriptor<VF> descriptor, GlProgram program, int initialBufferSize) {
        this.descriptor = descriptor;
        this.buffer = new BufferWriter(initialBufferSize);
        this.indicesBuffer = new BufferWriter(initialBufferSize);
        this.program = program;

        this.vbo.bind();
        this.vao.bind();
        this.ebo.bind();

        descriptor.prepareAttributes(program::getAttributeLocation);

        this.vao.unbind();
        this.vbo.unbind();
        this.ebo.unbind();

        this.builder = descriptor.createBuilder(this.buffer);
    }

    public void upload(boolean dynamic) {
        this.vbo.upload(this.buffer, dynamic);
        this.ebo.upload(this.indicesBuffer, dynamic);
    }

    public void clear() {
        this.buffer.rewind();
        this.indicesBuffer.rewind();
    }

    public void draw() {
        this.vao.bind();
        glDrawElements(GL_TRIANGLES, this.indicesBuffer.data.position() / 4, GL_UNSIGNED_INT, 0);
        this.vao.unbind();
    }

    public void delete() {
        this.vbo.delete();
        this.vao.delete();
        this.ebo.delete();
        this.buffer.free();
        this.indicesBuffer.free();
    }


    public BufferWriter getIndicesBuffer() {
        return this.indicesBuffer;
    }
}
