package com.chyzman;

import com.chyzman.object.GameObject;
import com.chyzman.object.Player;
import com.chyzman.render.Renderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;

import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class Game {

    public static Window window;
    public static Renderer renderer;

    private List<GameObject> gameObjects = new ArrayList<>();

    public static void main(String[] args) {
        System.load("C:\\Program Files\\RenderDoc\\renderdoc.dll");
        var game = new Game();
        game.run();
    }

    public void run() {
        window = new Window(640, 480);

        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);

        System.out.println("The um, the uh game is uh its running yeah.");
        GL43.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
            System.out.println("GL CALLBACK: " +
                    "source = 0x" + Integer.toHexString(source) + ", " +
                    "type = 0x" + Integer.toHexString(type) + ", " +
                    "id = " + id + ", " +
                    "severity = 0x" + Integer.toHexString(severity) + ", " +
                    "message = " + GLDebugMessageCallback.getMessage(length, message));
        }, NULL);

        renderer = new Renderer();

        var player = addGameObject(new Player());

        loop();
        window.terminate();
    }

    private void loop() {
        while(!window.shouldClose()) {
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