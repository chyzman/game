package com.chyzman.gl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL45.*;

public class GlShader {

    public final int id;
    public GlShader(int id) {
        this.id = id;
    }

    public void delete() {
        glDeleteShader(id);
    }

    public static GlShader vertex(InputStream source) {
        return loadAndCompile(source, glCreateShader(GL_VERTEX_SHADER));
    }

    public static GlShader fragment(InputStream source) {
        return loadAndCompile(source, glCreateShader(GL_FRAGMENT_SHADER));
    }

    public static GlShader vertex(String path) throws IOException {
        return loadAndCompile(Files.newInputStream(Path.of("src/main/resources/shaders/" + path)), glCreateShader(GL_VERTEX_SHADER));
    }

    public static GlShader fragment(String path) throws IOException {
        return loadAndCompile(Files.newInputStream(Path.of("src/main/resources/shaders/" + path)), glCreateShader(GL_FRAGMENT_SHADER));
    }

    private static GlShader loadAndCompile(InputStream source, int id) {
        String sourceAsString;
        try {
            sourceAsString = new String(source.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("why did we choose this language", e);
        }

        glShaderSource(id, sourceAsString);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException("The shader does not shader: " + glGetShaderInfoLog(id));
        }

        return new GlShader(id);
    }
}
