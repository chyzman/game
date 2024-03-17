package com.chyzman.gl;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;

import java.util.Deque;
import java.util.LinkedList;

public class MatrixStack {

    private final Deque<Matrix4f> matrices = new LinkedList<>();

    public MatrixStack() {
        this.matrices.push(new Matrix4f());
    }

    public MatrixStack push() {
        this.matrices.push(new Matrix4f(this.peek()));
        return this;
    }

    public MatrixStack translate(float x, float y, float z) {
        this.peek().translate(x, y, z);
        return this;
    }

    public MatrixStack scale(float x, float y, float z) {
        this.peek().scale(x, y, z);
        return this;
    }

    public MatrixStack rotate(float angle, Vector3fc axis) {
        this.peek().rotate(angle, axis);
        return this;
    }

    public MatrixStack mul(Matrix4fc matrix) {
        this.peek().mul(matrix);
        return this;
    }

    public Matrix4f peek() {
        return this.matrices.peek();
    }

    public MatrixStack pop() {
        this.matrices.pop();
        return this;
    }
}
