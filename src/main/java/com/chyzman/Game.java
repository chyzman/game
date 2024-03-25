package com.chyzman;

import com.chyzman.component.Named;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.Frameworks;
import com.chyzman.gl.GlDebug;
import com.chyzman.object.BasicObject;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.render.Renderer;
import com.chyzman.systems.CameraControl;
import com.chyzman.systems.MeshRenderer;
import com.chyzman.systems.Physics;
import com.chyzman.util.LogUtils;
import com.chyzman.world.World;
import com.chyzman.world.block.Blocks;
import com.chyzman.world.chunk.Chunk;
import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Entity;
import dev.dominion.ecs.api.Scheduler;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL45;
import org.slf4j.Logger;

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Game {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final int LOGIC_TICK_RATE = 64;

    public static final Game GAME = new Game();
    public static Window window;
    public static Renderer renderer;
    public static Entity camera;

    public FramedDominion dominion;
    public Scheduler clientScheduler;
    public Scheduler logicScheduler;
    public World world;
    public final Chunk chunk = new Chunk(0, 0, 0);

    public static final Deque<Runnable> runnables = new ConcurrentLinkedDeque<>();

    public final Thread clientThread = Thread.currentThread();

    public static void main(String[] args) {
        GAME.run();
    }

    public void run() {
        System.setProperty("dominion.show-banner", "false");
        dominion = new FramedDominion(Dominion.create());
        clientScheduler = dominion.createScheduler();
        logicScheduler = dominion.createScheduler();
        camera = dominion.createEntity(new Named("camera"), new Position(), new Rotation(), new CameraConfiguration());
        window = new Window(dominion, 640, 480);

        GlDebug.attachDebugCallback();
        GlDebug.minSeverity(GL45.GL_DEBUG_SEVERITY_LOW);

        Random random = new Random();
        var noise = JNoise.newBuilder().perlin(random.nextInt(), Interpolation.LINEAR, FadeFunction.IMPROVED_PERLIN_NOISE)
                .scale(1 / 16.0)
                .addModifier(v -> (v + 1) / 2.0)
                .clamp(0.0, 1.0)
                .build();

        world = new World(dominion);

        clientScheduler.schedule(CameraControl.create(dominion, clientScheduler::deltaTime));
        clientScheduler.schedule(MeshRenderer.create(dominion, clientScheduler::deltaTime));

        logicScheduler.schedule(Physics.create(dominion, logicScheduler::deltaTime));

        int chunksSize = 5;
        for (int xRad = 0; xRad < chunksSize; xRad++) {
            for (int yRad = 0; yRad < chunksSize; yRad++) {
                for (int zRad = 0; zRad < chunksSize; zRad++) {
                    for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                            for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                                if (noise.evaluateNoise(xRad * Chunk.CHUNK_SIZE + x, yRad * Chunk.CHUNK_SIZE + y, zRad * Chunk.CHUNK_SIZE + z) > .5)
                                    world.setBlock(xRad * Chunk.CHUNK_SIZE + x, yRad * Chunk.CHUNK_SIZE + y, zRad * Chunk.CHUNK_SIZE + z, Blocks.WHITE);
                            }
                        }
                    }
                }
            }
        }

        world.setBlock(0, 0, 0, Blocks.WHITE);
        world.setBlock(2, 0, 0, Blocks.WHITE);
        world.setBlock(1, 1, 0, Blocks.WHITE);
        world.setBlock(1, 2, 0, Blocks.WHITE);
        world.setBlock(1, 3, 0, Blocks.WHITE);

        renderer = new Renderer(window, dominion);

//        dominion.createEntity(Frameworks.POSITIONED_ENTITY, new Named("cube"), new MeshComponent("chyzman", new Id("game", "chyzman.png")), new Floatly());

        //TODO make this into a component for glisco
//        addGameObject(new EpiclyRenderedTriangle());
        // R.I.P EpiclyRenderedTriangle
        // you will be missed
        // 2024 - 2024
        // cause of death: not being used
        // EpiclyRenderedTriangle was a good object, it was a good object that was never used
        // it was one of the best objects that was never used
        // we will miss you EpiclyRenderedTriangle
        // you were a good object
        // *insert sad music here*

        dominion.createEntity(Frameworks.UNIQUE_ENTITY, new Named("test_grass"), new BasicObject());

        GLFW.glfwMaximizeWindow(window.handle);

        logicScheduler.tickAtFixedRate(LOGIC_TICK_RATE);

        loop(dominion);

        logicScheduler.shutDown();
        clientScheduler.shutDown();
        window.terminate();
    }

    public void runOnReaderThread(Runnable runnable) {
        if(Thread.currentThread() == Game.GAME.clientThread){
            runnable.run();
        } else {
            runnables.push(runnable);
        }
    }

    private void loop(FramedDominion dom) {
        while (!window.shouldClose()) {
            var runnable = runnables.peek();

            while (runnable != null) {
                runnables.pop();

                runnable.run();

                runnable = runnables.peek();
            }

            renderer.clear();
            clientScheduler.tick();
            renderer.update(dom);

            window.update();
        }
    }
}