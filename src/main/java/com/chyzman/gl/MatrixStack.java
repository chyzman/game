package com.chyzman.gl;

import org.joml.Matrix4f;

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

    public Matrix4f peek() {
        return this.matrices.peek();
    }

    public MatrixStack pop() {
        this.matrices.pop();
        return this;
    }
}
