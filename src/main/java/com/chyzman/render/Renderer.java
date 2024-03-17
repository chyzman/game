package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.gl.GlProgram;
import com.chyzman.gl.GlShader;
import com.chyzman.systems.Chunk;
import com.chyzman.systems.TextRenderer;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL45.*;

public class Renderer {
    public static final GlProgram POS_COLOR_TEXTURE_PROGRAM;
    public static final GlProgram POS_COLOR_TEXTURE_NORMAL_PROGRAM = null;
    public static final GlProgram FONT_PROGRAM;
    private final RenderChunk chunk = new RenderChunk(Game.GAME.chunk);
    public double deltaTime = 0.0f;	// Time between current frame and last frame
    public double lastFrame = 0.0f; // Time of last frame
    public double lastTime = 0.0f;
    private double framesPerSecond = 0.0f;
    public int fps;
    public static Vector3d cameraPosition = new Vector3d(0.0f, 0.0f, 0.0f);
    public TextRenderer textRenderer = new TextRenderer();

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void update() {
        double currentFrame = GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        chunk.draw();

        for(var gameObject : Game.GAME.gameObjects) {
            gameObject.update();
        }

        ++framesPerSecond;
        if (currentFrame - lastTime > 1.0f) {
            lastTime = currentFrame;
            fps = (int) framesPerSecond;
            framesPerSecond = 0;
        }

        renderDebug();

    }

    public void renderDebug() {
        var polygonMode = glGetInteger(GL_POLYGON_MODE);
        if (polygonMode == GL_LINE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        textRenderer.renderText("FPS: " + fps, 2.0f, 3.0f, 0.25f, new Vector3f(1f, 1f, 1f));
        textRenderer.renderText("Pos:(" + cameraPosition.x + ", " + cameraPosition.y + ", " + cameraPosition.z + ")", 2.0f, 26.0f, 0.25f, new Vector3f(1f, 1f, 1f));
        if (polygonMode == GL_LINE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
    }

    static {
        try {
            try {
                POS_COLOR_TEXTURE_PROGRAM = new GlProgram(
                        "position",
                        GlShader.vertex(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.vert"))),
                        GlShader.fragment(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.frag")))
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            try {
//                POS_COLOR_TEXTURE_NORMAL_PROGRAM = new GlProgram(
//                        "position",
//                        GlShader.vertex(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.vert"))),
//                        GlShader.fragment(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.frag")))
//                );
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            FONT_PROGRAM = new GlProgram(
                    "font",
                    GlShader.vertex("font.vert"),
                    GlShader.fragment("font.frag")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}