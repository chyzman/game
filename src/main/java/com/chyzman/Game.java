package com.chyzman;

import com.chyzman.gl.GlDebug;
import com.chyzman.object.Camera;
import com.chyzman.object.GameObject;
import com.chyzman.object.components.CoolCube;
import com.chyzman.object.components.EpiclyRenderedTriangle;
import com.chyzman.render.Renderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL45;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final Game GAME = new Game();

    public static Window window;
    public static Renderer renderer;

    public final List<GameObject> gameObjects = new ArrayList<>();
    public final Camera camera = new Camera();

    public static void main(String[] args) {
//        System.load("/home/alpha/Desktop/renderdoc_1.25/lib/librenderdoc.so");
        GAME.run();
    }

    public void run() {
        window = new Window(640, 480);
        GlDebug.attachDebugCallback();
        GlDebug.minSeverity(GL45.GL_DEBUG_SEVERITY_LOW);

        renderer = new Renderer();

//        var player = addGameObject(new Player());
        addGameObject(camera);
        addGameObject(new CoolCube());
        addGameObject(new EpiclyRenderedTriangle());

        loop();
        window.terminate();
    }

    private void loop() {
        while(!window.shouldClose()) {
            float currentFrame = (float) GLFW.glfwGetTime();
            renderer.clear();

            renderer.update();

            window.update();
        }
    }

    public GameObject addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        return gameObject;
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }
}