package com.chyzman;

import com.chyzman.component.position.Position;
import com.chyzman.gl.GlDebug;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.GameObject;
import com.chyzman.object.components.CoolCube;
import com.chyzman.object.components.EpiclyRenderedTriangle;
import com.chyzman.render.Renderer;
import com.chyzman.systems.Physics;
import com.chyzman.systems.CameraControl;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;
import org.lwjgl.opengl.GL45;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int LOGIC_TICK_RATE = 64;

    public static final Game GAME = new Game();

    public static Window window;
    public static Renderer renderer;

    public Dominion dominion;
    public Scheduler clientScheduler;
    public Scheduler logicScheduler;
    public final List<GameObject> gameObjects = new ArrayList<>();

    public static void main(String[] args) {
//        System.load("/home/alpha/Desktop/renderdoc_1.25/lib/librenderdoc.so");
        GAME.run();
    }

    public void run() {
        System.setProperty("dominion.show-banner", "false");
        dominion = Dominion.create();
        clientScheduler = dominion.createScheduler();
        logicScheduler = dominion.createScheduler();

        window = new Window(dominion, 640, 480);
        GlDebug.attachDebugCallback();
        GlDebug.minSeverity(GL45.GL_DEBUG_SEVERITY_LOW);

        renderer = new Renderer();

//        var player = addGameObject(new Player());

        dominion.createEntity("camera", new Position(), new CameraConfiguration());
        addGameObject(new CoolCube());
        addGameObject(new EpiclyRenderedTriangle());

        clientScheduler.schedule(CameraControl.create(dominion));
        logicScheduler.schedule(() -> Physics.update(dominion));

        loop();

        logicScheduler.tickAtFixedRate(LOGIC_TICK_RATE);
        window.terminate();
    }

    private void loop() {
        while(!window.shouldClose()) {
            renderer.clear();
            clientScheduler.tick();
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