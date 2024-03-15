package com.chyzman.old_i_think;

import org.joml.Matrix4f;
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

        window = GLFW.glfwCreateWindow(width, height, "Game", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Unable to create GLFW Window");
        }

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (window == this.window) {
                float xa = 0;
                float ya = 0;
                float za = 0;
                if (key == GLFW.GLFW_KEY_W) {
                    xa = 1.0F;
                }
                if (key == GLFW.GLFW_KEY_S) {
                    xa = -1.0F;
                }

                float dist = xa * xa + za * za;
                if (dist < 0.01f) {
                    return;
                }
                dist = (float)Math.sqrt(dist);

                float sin = (float)Math.sin((double)Game.GAME.camera.yaw * Math.PI / 180.0);
                float cos = (float)Math.cos((double)Game.GAME.camera.yaw * Math.PI / 180.0);
                Game.GAME.camera.deltaPos.add((xa *= dist) * cos - (za *= dist) * sin, 0, za * cos + xa * sin);
            }
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

        GLFW.glfwSetWindowSizeCallback(window, windowSize = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Game.window.height = height;
                Game.window.width = width;
                Game.window.aspectRatio = width / height;
                GL11.glViewport(0, 0, Game.window.width, Game.window.height);
            }
        });

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
        return new Matrix4f().ortho2D(-this.width/2f, this.width/2f, -this.height/2f, this.height/2f);
    }
}