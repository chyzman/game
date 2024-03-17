package com.chyzman.gl;

import com.chyzman.ui.core.Color;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

public final class VertexDescriptors {

    public static final VertexDescriptor<PosVertexFunction> POSITION = new VertexDescriptor<>(
            attributeConsumer -> attributeConsumer.attribute("aPos", VertexElementType.FLOAT, 3),
            writer -> writer::float3
    );

    public static final VertexDescriptor<PosColorVertexFunction> POSITION_COLOR = new VertexDescriptor<>(
            attributeConsumer -> {
                attributeConsumer.attribute("aPos", VertexElementType.FLOAT, 3);
                attributeConsumer.attribute("aColor", VertexElementType.FLOAT, 4);
            },
            writer -> (x, y, z, a, r, g, b) -> {
                writer.float3(x, y, z);
                writer.float4(r, g, b, a);
            }
    );

    public static final VertexDescriptor<TextVertexFunction> FONT = new VertexDescriptor<>(
            attributeConsumer -> attributeConsumer.attribute("vertex", VertexElementType.FLOAT, 4),
            writer -> writer::float4
    );

    @FunctionalInterface
    public interface PosVertexFunction {
        void vertex(float x, float y, float z);
        default void vertex(Vector3fc vertex) {
            this.vertex(vertex.x(), vertex.y(), vertex.z());
        }
    }

    @FunctionalInterface
    public interface PosColorVertexFunction {
        void vertex(float x, float y, float z, float a, float r, float g, float b);
        default void vertex(Vector3fc vertex, Color color) {
            this.vertex(vertex.x(), vertex.y(), vertex.z(), color.alpha(), color.red(), color.green(), color.blue());
        }
    }

    @FunctionalInterface
    public interface TextVertexFunction {
        void vertex(float x, float y, float z, float w);
        default void vertex(Vector4fc vertex) {
            this.vertex(vertex.x(), vertex.y(), vertex.z(), vertex.w());
        }
    }
}
