package com.chyzman;

import com.chyzman.component.position.Position;
import com.chyzman.gl.GlDebug;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.GameObject;
import com.chyzman.object.components.CoolCube;
import com.chyzman.object.components.EpiclyRenderedTriangle;
import com.chyzman.render.Renderer;
import com.chyzman.systems.Chunk;
import com.chyzman.systems.Physics;
import com.chyzman.systems.CameraControl;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Scheduler;
import org.lwjgl.opengl.GL45;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static final int LOGIC_TICK_RATE = 64;

    public static final Game GAME = new Game();
    public static Window window;
    public static Renderer renderer;

    public Dominion dominion;
    public Scheduler clientScheduler;
    public Scheduler logicScheduler;
    public final List<GameObject> gameObjects = new ArrayList<>();
    public final Chunk chunk = new Chunk(0, 0, 0);

    public static void main(String[] args) {
//        System.load("/home/alpha/Desktop/renderdoc_1.25/lib/librenderdoc.so");
        GAME.run();
    }

    public void run() {
        System.setProperty("dominion.show-banner", "false");
        dominion = Dominion.create();
        clientScheduler = dominion.createScheduler();
        logicScheduler = dominion.createScheduler();
        dominion.createEntity("camera", new Position(), new CameraConfiguration());
        window = new Window(dominion, 640, 480);
        GlDebug.attachDebugCallback();
        GlDebug.minSeverity(GL45.GL_DEBUG_SEVERITY_LOW);

        Random random = new Random();
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                chunk.setBlock(x, random.nextInt(5), z, (byte) 1);
            }
        }

        renderer = new Renderer();

//        var player = addGameObject(new Player());


//        addGameObject(new CoolCube());
        addGameObject(new EpiclyRenderedTriangle());

        clientScheduler.schedule(CameraControl.create(dominion, clientScheduler::deltaTime));
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