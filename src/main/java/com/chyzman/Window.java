package com.chyzman;

import com.chyzman.component.position.Position;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.object.CameraConfiguration;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static java.sql.Types.NULL;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Window {
    public long handle;
    public int width;
    public int height;
    public int aspectRatio;
    private boolean wireframe = false;
    private final MouseManager mouseManager = new MouseManager();

    private GLFWWindowSizeCallback windowSize;

    private final FramedDominion dominion;

    public Window(FramedDominion dominion, int width, int height) {
        this.dominion = dominion;

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

        handle = GLFW.glfwCreateWindow(width, height, "Game", NULL, NULL);
        if (handle == NULL) {
            throw new IllegalStateException("Unable to create GLFW Window");
        }

        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (window == this.handle && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
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

        GLFW.glfwSetCursorPosCallback(this.handle, (win, xpos, ypos) -> {
            if (win == this.handle)
                mouseManager.setCursorPos(dominion, xpos, ypos);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

            GLFW.glfwMakeContextCurrent(handle);
            GLFW.glfwSwapInterval(1);
            GLFW.glfwShowWindow(handle);
        }

        GLFW.glfwSetScrollCallback(handle, (win, xOffset, yOffset) -> {
            if (handle == win) {

                for (var camera : dominion.findEntitiesWith(Position.class, CameraConfiguration.class).comp2Iterable()) {
                    camera.cameraSpeed *= 1 + (yOffset / 10);
                }
            }
        });

        GLFW.glfwSwapInterval(0);

        GLFW.glfwSetWindowSizeCallback(handle, windowSize = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                for (var camera : dominion.findEntitiesWith(Position.class, CameraConfiguration.class).comp2Iterable()) {
                    Game.window.height = height;
                    Game.window.width = width;
                    Game.window.aspectRatio = width / height;
                    GL11.glViewport(0, 0, Game.window.width, Game.window.height);
//                Game.renderer.getProjectionMatrix().ortho2D(-width/2f, width/2f, -height/2f, height/2f);
                    Game.renderer.getProjectionMatrix().peek().setPerspective((float)Math.toRadians(camera.fov), (float) width / (float) height, 0.1f, 1000f);
                }
            }
        });

        GL.createCapabilities();
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void terminate() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public boolean mouseGrabbed() {
        return mouseManager.isGrabbed();
    }
}