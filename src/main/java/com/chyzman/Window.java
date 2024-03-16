package com.chyzman;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static java.sql.Types.NULL;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Window {
    public long window;
    public int width;
    public int height;
    public int aspectRatio;
    private boolean wireframe = false;
    private final MouseManager mouseManager = new MouseManager();
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f transformMatrix = new Matrix4f();
    private final Matrix4f modelMatrix = new Matrix4f();

    private GLFWWindowSizeCallback windowSize;

    public Window(int width, int height) {
        init(width, height);
    }

    private void init(int width, int height) {
        this.width = width;
        this.height = height;
        this.aspectRatio = width / height;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 16);

        window = GLFW.glfwCreateWindow(width, height, "Game", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Unable to create GLFW Window");
        }

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (window == this.window && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
                if (key == GLFW.GLFW_KEY_T) {
                    mouseManager.toggleGrabbed();
                }

                if (key == GLFW.GLFW_KEY_B) {
                    if (wireframe)
                        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                    else
                        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                    wireframe = !wireframe;
                }
            }
        });

        GLFW.glfwSetCursorPosCallback(this.window, (win, xpos, ypos) -> {
            if (win == this.window)
                mouseManager.setCursorPos(xpos, ypos);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

            GLFW.glfwMakeContextCurrent(window);
            GLFW.glfwSwapInterval(1);
            GLFW.glfwShowWindow(window);
        }

        GLFW.glfwSetScrollCallback(window, (win, xOffset, yOffset) -> {
            if (window == win) {
                Game.GAME.camera.cameraSpeed *= 1 + (yOffset / 10);
            }
        });

        GLFW.glfwSetWindowSizeCallback(window, windowSize = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Game.window.height = height;
                Game.window.width = width;
                Game.window.aspectRatio = width / height;
                GL11.glViewport(0, 0, Game.window.width, Game.window.height);
//                projectionMatrix.ortho2D(-width/2f, width/2f, -height/2f, height/2f);
                projectionMatrix.setPerspective((float)Math.toRadians(Game.GAME.camera.fov), (float) width / (float) height, 0.1f, 1000f);
            }
        });
//        projectionMatrix.ortho2D(-this.width/2f, this.width/2f, -this.height/2f, this.height/2f);
        projectionMatrix.perspective((float)Math.toRadians(Game.GAME.camera.fov), (float) width / (float) height, 0.1f, 1000f);

        GL.createCapabilities();
    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void terminate() {
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return transformMatrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}