package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.systems.TextRenderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Renderer {
    public double deltaTime = 0.0f;	// Time between current frame and last frame
    public double lastFrame = 0.0f; // Time of last frame
    public double lastTime = 0.0f;
    private double framesPerSecond = 0.0f;
    public int fps;
    public TextRenderer textRenderer = new TextRenderer();

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void update() {
        double currentFrame = GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

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
        textRenderer.renderText("FPS: " + fps, 25.0f, 25.0f, 1.0f, new Vector3f(0.5F, 0.8F, 0.2F));
    }

}