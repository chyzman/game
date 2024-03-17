package com.chyzman.util;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public record Vec3i(int x, int y, int z) implements Vector3ic {
    @Override
    public IntBuffer get(IntBuffer buffer) {
        return null;
    }

    @Override
    public IntBuffer get(int index, IntBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer get(ByteBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer get(int index, ByteBuffer buffer) {
        return null;
    }

    @Override
    public Vector3ic getToAddress(long address) {
        return null;
    }

    @Override
    public Vector3i sub(Vector3ic v, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i sub(int x, int y, int z, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i add(Vector3ic v, Vector3i dest) {
        dest.x = x + v.x();
        dest.y = y + v.y();
        dest.z = z + v.z();
        return dest;
    }

    @Override
    public Vector3i add(int x, int y, int z, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i mul(int scalar, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i mul(Vector3ic v, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i mul(int x, int y, int z, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i div(float scalar, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i div(int scalar, Vector3i dest) {
        return null;
    }

    @Override
    public long lengthSquared() {
        return 0;
    }

    @Override
    public double length() {
        return 0;
    }

    @Override
    public double distance(Vector3ic v) {
        return 0;
    }

    @Override
    public double distance(int x, int y, int z) {
        return 0;
    }

    @Override
    public long gridDistance(Vector3ic v) {
        return 0;
    }

    @Override
    public long gridDistance(int x, int y, int z) {
        return 0;
    }

    @Override
    public long distanceSquared(Vector3ic v) {
        return 0;
    }

    @Override
    public long distanceSquared(int x, int y, int z) {
        return 0;
    }

    @Override
    public Vector3i negate(Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i min(Vector3ic v, Vector3i dest) {
        return null;
    }

    @Override
    public Vector3i max(Vector3ic v, Vector3i dest) {
        return null;
    }

    @Override
    public int get(int component) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public int maxComponent() {
        return 0;
    }

    @Override
    public int minComponent() {
        return 0;
    }

    @Override
    public Vector3i absolute(Vector3i dest) {
        return null;
    }

    @Override
    public boolean equals(int x, int y, int z) {
        return false;
    }
}
