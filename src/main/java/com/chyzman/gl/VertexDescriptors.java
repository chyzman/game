package com.chyzman.gl;

import org.joml.Vector3fc;

public final class VertexDescriptors {

    public static final VertexDescriptor<PosVertexFunction> POSITION = new VertexDescriptor<>(
            attributeConsumer -> attributeConsumer.attribute("aPos", VertexElementType.FLOAT, 3),
            writer -> writer::float3
    );

    @FunctionalInterface
    public interface PosVertexFunction {
        void vertex(float x, float y, float z);
        default void vertex(Vector3fc vertex) {
            this.vertex(vertex.x(), vertex.y(), vertex.z());
        }
    }
}
