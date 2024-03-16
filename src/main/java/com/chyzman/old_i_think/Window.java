package com.chyzman.old_i_think;

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

        window = GLFW.glfwCreateWindow(width, height, "Game", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Unable to create GLFW Window");
        }

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (window == this.window && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
                float xa = 0;
                float ya = 0;
                float za = 0;
                var camera = Game.GAME.camera;
                var cameraPos = camera.pos();

                float cameraSpeed = 2.5f * Game.GAME.deltaTime;
                if (key == GLFW.GLFW_KEY_W) {
                    cameraPos.add(new Vector3f(camera.cameraFront).mul(cameraSpeed));
                }
                if (key == GLFW.GLFW_KEY_S) {
                    cameraPos.sub(new Vector3f(camera.cameraFront).mul(cameraSpeed));
                }
                if (key == GLFW.GLFW_KEY_A) {
                    cameraPos.sub(new Vector3f(camera.cameraFront).cross(camera.cameraUp).normalize().mul(cameraSpeed));
                }
                if (key == GLFW.GLFW_KEY_D) {
                    cameraPos.add(new Vector3f(camera.cameraFront).cross(camera.cameraUp).normalize().mul(cameraSpeed));
                }

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

        GLFW.glfwSetWindowSizeCallback(window, windowSize = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Game.window.height = height;
                Game.window.width = width;
                Game.window.aspectRatio = width / height;
                GL11.glViewport(0, 0, Game.window.width, Game.window.height);
//                projectionMatrix.ortho2D(-width/2f, width/2f, -height/2f, height/2f);
            }
        });
//        projectionMatrix.ortho2D(-this.width/2f, this.width/2f, -this.height/2f, this.height/2f);
        projectionMatrix.perspective((float)Math.toRadians(45), (float) width / (float) height, 0.1f, 100f);

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
        return transformMatrix;
    }
}