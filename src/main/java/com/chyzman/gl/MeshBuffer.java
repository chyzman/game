package com.chyzman.gl;

public class MeshBuffer<VF> {

    private final GlVertexBuffer vbo = new GlVertexBuffer();
    private final GlVertexArray vao = new GlVertexArray();

    private final BufferWriter buffer;
    private final VertexDescriptor<VF> descriptor;

    public final GlProgram program;
    public final VF builder;

    public MeshBuffer(VertexDescriptor<VF> descriptor, GlProgram program) {
        this(descriptor, program, 1024);
    }

    public MeshBuffer(VertexDescriptor<VF> descriptor, GlProgram program, int initialBufferSize) {
        this.descriptor = descriptor;
        this.buffer = new BufferWriter(initialBufferSize);
        this.program = program;

        this.vbo.bind();
        this.vao.bind();

        descriptor.prepareAttributes(program::getAttributeLocation);

        this.vao.unbind();
        this.vbo.unbind();

        this.builder = descriptor.createBuilder(this.buffer);
    }

    public void upload(boolean dynamic) {
        this.vbo.upload(this.buffer, dynamic);
    }

    public void clear() {
        this.buffer.rewind();
    }

    public void draw() {
        this.vao.draw(this.buffer.data.position() / this.descriptor.vertexSize());
    }

    public void delete() {
        this.vbo.delete();
        this.vao.delete();
        this.buffer.free();
    }
}
