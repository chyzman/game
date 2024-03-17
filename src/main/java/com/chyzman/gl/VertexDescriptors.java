package com.chyzman.gl;

import com.chyzman.ui.core.Color;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

public final class VertexDescriptors {

    public static final VertexDescriptor<PosVertexFunction> POSITION = new VertexDescriptor<>(
            attributeConsumer -> attributeConsumer.attribute("aPos", VertexElementType.FLOAT, 3),
            writer -> writer::float3
    );

    public static final VertexDescriptor<PosColorTexVertexFunction> POSITION_COLOR_TEX = new VertexDescriptor<>(
            attributeConsumer -> {
                attributeConsumer.attribute("aPos", VertexElementType.FLOAT, 3);
                attributeConsumer.attribute("aColor", VertexElementType.FLOAT, 3);
                attributeConsumer.attribute("aTexCoord", VertexElementType.FLOAT, 2);
            },
            writer -> (x, y, z, r, g, b, u, v) -> {
                writer.float3(x, y, z);
                writer.float3(r, g, b);
                writer.float2(u, v);
            }
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

    public interface PosColorTexVertexFunction {
        void vertex(float x, float y, float z, float r, float g, float b, float u, float v);
        default void vertex(Vector4fc vertex, float r, float g, float b, float u, float v) {
            this.vertex(vertex.x(), vertex.y(), vertex.z(), r, g, b, u, v);
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
