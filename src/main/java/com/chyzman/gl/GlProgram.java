package com.chyzman.gl;

import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL45.*;

public class GlProgram {

    public final int id;
    public final String name;
    private final Map<String, Integer> uniformCache = new HashMap<>();

    public GlProgram(String name, GlShader... shaders) {
        this.name = name;
        this.id = glCreateProgram();

        for (var shader : shaders) {
            glAttachShader(this.id, shader.id);
        }

        glLinkProgram(this.id);

        for (var shader : shaders) {
            shader.delete();
        }

        if (glGetProgrami(this.id, GL_LINK_STATUS) != GL_TRUE) {
            throw new RuntimeException("The program does not program: " + glGetProgramInfoLog(id));
        }
    }

    public void use() {
        glUseProgram(this.id);
    }

    public void uniformMat4(String uniform, Matrix4fc value) {
        try (var stack = MemoryStack.stackPush()) {
            var buffer = stack.mallocFloat(16);
            value.get(buffer);

            glUniformMatrix4fv(this.uniformLocation(uniform), false, buffer);
        }
    }

    public void uniform1f(String uniform, float value) {
        glUniform1f(this.uniformLocation(uniform), value);
    }

    public void uniform2f(String uniform, Vector2fc value) {
        this.uniform2f(uniform, value.x(), value.y());
    }

    public void uniform2f(String uniform, float a, float b) {
        glUniform2f(this.uniformLocation(uniform), a, b);
    }

    public void uniform3f(String uniform, Vector3fc value) {
        this.uniform3f(uniform, value.x(), value.y(), value.z());
    }

    public void uniform3f(String uniform, float a, float b, float c) {
        glUniform3f(this.uniformLocation(uniform), a, b, c);
    }

    private int uniformLocation(String uniform) {
        return this.uniformCache.computeIfAbsent(uniform, uniformName -> glGetUniformLocation(this.id, uniformName));
    }

    public int getAttributeLocation(String attribute) {
        return glGetAttribLocation(this.id, attribute);
    }
}

