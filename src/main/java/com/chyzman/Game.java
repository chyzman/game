package com.chyzman;

import com.chyzman.object.Camera;
import com.chyzman.object.GameObject;
import com.chyzman.object.components.CoolCube;
import com.chyzman.object.components.EpiclyRenderedTriangle;
import com.chyzman.render.Renderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;

import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class Game {
    public static final Game GAME = new Game();

    public static Window window;
    public static Renderer renderer;
    public float deltaTime = 0.0f;	// Time between current frame and last frame
    public float lastFrame = 0.0f; // Time of last frame

    private final List<GameObject> gameObjects = new ArrayList<>();
    public final Camera camera = new Camera();

    public static void main(String[] args) {
//        System.load("/home/alpha/Desktop/renderdoc_1.25/lib/librenderdoc.so");
        GAME.run();
    }

    public void run() {
        window = new Window(640, 480);

        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        GL43.glDebugMessageCallback(GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
            System.out.println("GL CALLBACK: " +
                    "source = 0x" + Integer.toHexString(source) + ", " +
                    "type = 0x" + Integer.toHexString(type) + ", " +
                    "id = " + id + ", " +
                    "severity = 0x" + Integer.toHexString(severity) + ", " +
                    "message = " + GLDebugMessageCallback.getMessage(length, message));
        }), NULL);
        GL43.glDebugMessageInsert(GL43.GL_DEBUG_SOURCE_OTHER, GL43.GL_DEBUG_TYPE_OTHER, 0, GL43.GL_DEBUG_SEVERITY_HIGH, "bruh");

        System.err.println("The um, the uh game is uh its running yeah.");

        renderer = new Renderer();

//        var player = addGameObject(new Player());
        addGameObject(camera);
        addGameObject(new EpiclyRenderedTriangle());
        addGameObject(new CoolCube());

        loop();
        window.terminate();
    }

    private void loop() {
        while(!window.shouldClose()) {
            float currentFrame = (float) GLFW.glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;
            renderer.clear();

            for(var gameObject : gameObjects) {
                gameObject.update();
            }

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