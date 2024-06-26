package com.chyzman.gl;

import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class BufferWriter {

    private static final int FLOAT32_SIZE = 4;
    private static final int INT32_SIZE = 4;

    ByteBuffer data;

    public BufferWriter(int initialSize) {
        this.data = MemoryUtil.memAlloc(initialSize);
    }

    public BufferWriter() {
        this(64);
    }

    public void rewind() {
        this.data.rewind();
    }

    public void float2(float a, float b) {
        this.ensureCapacity(FLOAT32_SIZE * 2);
        this.data.putFloat(a).putFloat(b);
    }

    public void vec3(Vector3fc vec) {
        this.float3(vec.x(), vec.y(), vec.z());
    }

    public void float3(float a, float b, float c) {
        this.ensureCapacity(FLOAT32_SIZE * 3);
        this.data.putFloat(a).putFloat(b).putFloat(c);
    }

    public void vec4(Vector4fc vec) {
        this.float4(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public void float4(float a, float b, float c, float d) {
        this.ensureCapacity(FLOAT32_SIZE * 4);
        this.data.putFloat(a).putFloat(b).putFloat(c).putFloat(d);
    }

    public void int3(int a, int b, int c) {
        this.ensureCapacity(INT32_SIZE * 3);
        this.data.putInt(a).putInt(b).putInt(c);
    }

    public void free() {
        MemoryUtil.memFree(this.data);
    }

    private void ensureCapacity(int bytes) {
        if (this.data.remaining() >= bytes) return;
        this.data = MemoryUtil.memRealloc(this.data, this.data.capacity() * 2);

        System.out.println("Resizing buffer from " + this.data.capacity() + " bytes to " + this.data.capacity() * 2);
    }
}
