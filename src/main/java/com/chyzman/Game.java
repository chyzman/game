package com.chyzman;

import com.chyzman.object.Camera;
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
    public static final Game GAME = new Game();

    public static Window window;
    public static Renderer renderer;

    private final List<GameObject> gameObjects = new ArrayList<>();
    public final Camera camera = new Camera();

    public static void main(String[] args) {
        System.load("/home/alpha/Desktop/renderdoc_1.25/lib/librenderdoc.so");
        GAME.run();
    }

    public void run() {
        window = new Window(640, 480);

        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);

        System.out.println("The um, the uh game is uh its running yeah.");

        renderer = new Renderer();

        var player = addGameObject(new Player());
        addGameObject(camera);

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