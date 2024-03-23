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

    public static final VertexDescriptor<PosColorTexVertexFunction> POSITION_COLOR_TEXTURE = new VertexDescriptor<>(
            attributeConsumer -> {
                attributeConsumer.attribute("aPos", VertexElementType.FLOAT, 3);
                attributeConsumer.attribute("aColor", VertexElementType.FLOAT, 4);
                attributeConsumer.attribute("aTexCoord", VertexElementType.FLOAT, 2);
            },
            writer -> (x, y, z, a, r, g, b, u, v) -> {
                writer.float3(x, y, z);
                writer.float4(r, g, b, a);
                writer.float2(u, v);
            }
    );

    public static final VertexDescriptor<PosColorTexNormalVertexFunction> POSITION_COLOR_TEXTURE_NORMAL = new VertexDescriptor<>(
            attributeConsumer -> {
                attributeConsumer.attribute("aPos", VertexElementType.FLOAT, 3);
                attributeConsumer.attribute("aColor", VertexElementType.FLOAT, 4);
                attributeConsumer.attribute("aTexCoord", VertexElementType.FLOAT, 2);
                attributeConsumer.attribute("aNormal", VertexElementType.FLOAT, 3);
            },
            writer -> (x, y, z, a, r, g, b, u, v, normalX, normalY, normalZ) -> {
                writer.float3(x, y, z);
                writer.float4(r, g, b, a);
                writer.float2(u, v);
                writer.float3(normalX, normalY, normalZ);
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

    public interface PosColorTexVertexFunction {
        void vertex(float x, float y, float z, float a, float r, float g, float b, float u, float v);
        default void vertex(Vector3fc vertex, Color color, float u, float v) {
            this.vertex(vertex.x(), vertex.y(), vertex.z(), color.alpha(), color.red(), color.green(), color.blue(), u, v);
        }
    }

    public interface PosColorTexNormalVertexFunction {
        void vertex(float x, float y, float z, float a, float r, float g, float b, float u, float v, float normalX, float normalY, float normalZ);
        default void vertex(Vector3fc vertex, Color color, float u, float v, Vector3fc normal) {
            this.vertex(vertex.x(), vertex.y(), vertex.z(), color.alpha(), color.red(), color.green(), color.blue(), u, v, normal.x(), normal.y(), normal.z());
        }
        default void vertex(float x, float y, float z, Color color, float u, float v, float normalX, float normalY, float normalZ) {
            this.vertex(x, y, z, color.alpha(), color.red(), color.green(), color.blue(), u, v, normalX, normalY, normalZ);
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
