package com.chyzman;

import com.chyzman.component.Named;
import com.chyzman.component.position.Floatly;
import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.position.Velocity;
import com.chyzman.gl.GlDebug;
import com.chyzman.object.BasicObject;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.GameObject;
import com.chyzman.object.components.CoolCube;
import com.chyzman.object.components.EpiclyRenderedTriangle;
import com.chyzman.registry.Frameworks;
import com.chyzman.render.Renderer;
import com.chyzman.systems.CameraControl;
import com.chyzman.systems.DomSystem;
import com.chyzman.systems.Physics;
import com.chyzman.util.LogUtils;
import com.chyzman.ui.core.Color;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final int LOGIC_TICK_RATE = 64;

    public static final Game GAME = new Game();
    public static Window window;
    public static Renderer renderer;
    public static Entity camera;

    public Dominion dominion;
    public Scheduler clientScheduler;
    public Scheduler logicScheduler;
    public final List<GameObject> gameObjects = new ArrayList<>();
    public World world;
    public final Chunk chunk = new Chunk(0, 0, 0);

    public static void main(String[] args) {
        GAME.run();
    }

    public void run() {
        System.setProperty("dominion.show-banner", "false");
        dominion = Dominion.create();
        clientScheduler = dominion.createScheduler();
        logicScheduler = dominion.createScheduler();
        camera = dominion.createEntity("camera", new Position(), new CameraConfiguration());
        window = new Window(dominion, 640, 480);
        GlDebug.attachDebugCallback();
        GlDebug.minSeverity(GL45.GL_DEBUG_SEVERITY_LOW);

        Random random = new Random();
        var noise = JNoise.newBuilder().perlin(63610, Interpolation.LINEAR, FadeFunction.IMPROVED_PERLIN_NOISE)
                .scale(1 / 16.0)
                .addModifier(v -> (v + 1) / 2.0)
                .clamp(0.0, 1.0)
                .build();

        world = new World(dominion);

        for (int xRad = 0; xRad < 16; xRad++) {
            for (int yRad = 0; yRad < 16; yRad++) {
                for (int zRad = 0; zRad < 16; zRad++) {
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

//        world.setBlock(0, 0, 0, Blocks.WHITE);
//        world.setBlock(2, 0, 0, Blocks.WHITE);
//        world.setBlock(1, 1, 0, Blocks.WHITE);
//        world.setBlock(1, 2, 0, Blocks.WHITE);
//        world.setBlock(1, 3, 0, Blocks.WHITE);

        var comp = dominion.composition();
        var cow = comp.of(
                Position.class,
                Velocity.class,
                Gravity.class,
                BasicObject.class);

        dominion.createPreparedEntity(cow.withValue(new Position(), new Velocity(), new Gravity(), new BasicObject()));

        renderer = new Renderer(window, dominion);

        Frameworks.POSITIONED_ENTITY.addToWith(dominion, new Named("cube"), new CoolCube(), new Floatly());

        addGameObject(new EpiclyRenderedTriangle());

        Frameworks.UNIQUE_ENTITY.addToWith(dominion, new Named("test_grass"), new BasicObject());

        clientScheduler.schedule(CameraControl.create(dominion, clientScheduler::deltaTime));

        logicScheduler.schedule(DomSystem.create(dominion, "float", (dominion) -> {
            dominion.findEntitiesWith(Position.class, Floatly.class).forEach(result -> {
                Position pos = result.comp1();
                Floatly floatly = result.comp2();
                pos.set(Math.sin(GLFW.glfwGetTime()), Math.cos(GLFW.glfwGetTime()), pos.z);
                floatly.ticks++;
//            System.out.printf("\r%s: Pos:(%s, %s, %s) Vel:(%s,%s,%s)", result.entity().getName(), pos.x, pos.y, pos.z, velocity.x*(double)LOGIC_TICK_RATE, velocity.y*(double)LOGIC_TICK_RATE, velocity.z*(double)LOGIC_TICK_RATE);
            });
        }));

        logicScheduler.schedule(Physics.create(dominion, logicScheduler::deltaTime));

        logicScheduler.schedule(DomSystem.create(dominion, "test_grav", dom -> {
            for (var result : dom.findEntitiesWith(Named.class, Gravity.class)) {
                var named = result.comp1();

                if(!named.hasName() || !named.name().equals("test_grass")) return;

                var gravity = result.comp2();

                long window = Game.window.handle;

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
                    gravity.y += 0.00001;
                }

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
                    gravity.y -= 0.00001;
                }
            }
        }));

        logicScheduler.tickAtFixedRate(LOGIC_TICK_RATE);

        loop(dominion);

        logicScheduler.shutDown();
        clientScheduler.shutDown();
        window.terminate();
    }

    private void loop(Dominion dom) {
        while(!window.shouldClose()) {
            renderer.clear();
            clientScheduler.tick();
            renderer.update(dom);

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