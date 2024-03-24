package com.chyzman;

import com.chyzman.component.Named;
import com.chyzman.component.position.Floatly;
import com.chyzman.component.position.Gravity;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.dominion.Frameworks;
import com.chyzman.dominion.IdentifiedSystem;
import com.chyzman.gl.GlDebug;
import com.chyzman.object.BasicObject;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.GameObject;
import com.chyzman.object.components.EpiclyRenderedTriangle;
import com.chyzman.object.components.MeshComponent;
import com.chyzman.render.Renderer;
import com.chyzman.systems.CameraControl;
import com.chyzman.systems.MeshRenderer;
import com.chyzman.systems.Physics;
import com.chyzman.util.Id;
import com.chyzman.util.LogUtils;
import com.chyzman.world.World;
import com.chyzman.world.block.Blocks;
import com.chyzman.world.chunk.Chunk;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.objects.PhysicsBody;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.NativeLibraryLoader;
import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Entity;
import dev.dominion.ecs.api.Scheduler;
import org.joml.Quaterniond;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL45;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
    public final List<GameObject> gameObjects = new ArrayList<>();
    public World world;
    public PhysicsSpace physicsSpace;
    public final Chunk chunk = new Chunk(0, 0, 0);

    public static void main(String[] args) {
        GAME.run();
    }

    public void run() {
        System.setProperty("dominion.show-banner", "false");
        dominion = new FramedDominion(Dominion.create());
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

        NativeLibraryLoader.loadLibbulletjme(true, Path.of("src/main/resources/native").toFile(), "Debug", "Sp");

        physicsSpace = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);

        var plane = new Plane(Vector3f.UNIT_Y, -10);
        var planeCollision = new PlaneCollisionShape(plane);
        var floor = new PhysicsRigidBody(planeCollision, PhysicsBody.massForStatic);
        physicsSpace.addCollisionObject(floor);

//        physicsSpace.setGravity(new Vector3f(0, -0.1f, 0));

//        for (int xRad = 0; xRad < 16; xRad++) {
//            for (int yRad = 0; yRad < 16; yRad++) {
//                for (int zRad = 0; zRad < 16; zRad++) {
//                    for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
//                        for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
//                            for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
//                                if (noise.evaluateNoise(xRad * Chunk.CHUNK_SIZE + x, yRad * Chunk.CHUNK_SIZE + y, zRad * Chunk.CHUNK_SIZE + z) > .5)
//                                    world.setBlock(xRad * Chunk.CHUNK_SIZE + x, yRad * Chunk.CHUNK_SIZE + y, zRad * Chunk.CHUNK_SIZE + z, Blocks.WHITE);
//                            }
//                        }
//                    }
//                }
//            }
//        }

        world.setBlock(0, 0, 0, Blocks.WHITE);
        world.setBlock(2, 0, 0, Blocks.WHITE);
        world.setBlock(1, 1, 0, Blocks.WHITE);
        world.setBlock(1, 2, 0, Blocks.WHITE);
        world.setBlock(1, 3, 0, Blocks.WHITE);

        var cowCollision = new BoxCollisionShape(0.5f, 0.5f, 0.5f);
        var cowBox = new PhysicsRigidBody(cowCollision, 1);
        physicsSpace.addCollisionObject(cowBox);
        dominion.createEntity(Frameworks.PHYSICS_ENTITY, new Named("cow"), new Position(10, 0, 10), cowBox, new BasicObject(), new MeshComponent("chyzman", new Id("game", "chyzman.png")));

        renderer = new Renderer(window, dominion);

        dominion.createEntity(Frameworks.POSITIONED_ENTITY, new Named("cube"), new MeshComponent("chyzman", new Id("game", "chyzman.png")), new Floatly());

        addGameObject(new EpiclyRenderedTriangle());

        dominion.createEntity(Frameworks.UNIQUE_ENTITY, new Named("test_grass"), new BasicObject());

        clientScheduler.schedule(CameraControl.create(dominion, clientScheduler::deltaTime));
        clientScheduler.schedule(MeshRenderer.create(dominion, clientScheduler::deltaTime));

        logicScheduler.schedule(IdentifiedSystem.of(new Id("game", "float"), dominion, (dominion) -> {
            dominion.findEntitiesWith(Position.class, Floatly.class).forEach(result -> {
                Position pos = result.comp1();
                Floatly floatly = result.comp2();
                pos.set(Math.sin(GLFW.glfwGetTime()), Math.cos(GLFW.glfwGetTime()), pos.z);
                floatly.ticks++;
//            System.out.printf("\r%s: Pos:(%s, %s, %s) Vel:(%s,%s,%s)", result.entity().getName(), pos.x, pos.y, pos.z, velocity.x*(double)LOGIC_TICK_RATE, velocity.y*(double)LOGIC_TICK_RATE, velocity.z*(double)LOGIC_TICK_RATE);
            });
        }));

        logicScheduler.schedule(Physics.create(dominion, logicScheduler::deltaTime));

        logicScheduler.schedule(IdentifiedSystem.of(new Id("game", "physics"), dominion, dom -> {
            physicsSpace.update(1f/LOGIC_TICK_RATE);
            for (var result : dom.findEntitiesWith(PhysicsRigidBody.class, Position.class)) {
                var body = result.comp1();
                var pos = result.comp2();
                var p = body.getPhysicsLocation(new Vector3f());
                pos.set(p.x, p.y, p.z);
            }
            for (var result : dom.findEntitiesWith(PhysicsRigidBody.class, Rotation.class)) {
                var body = result.comp1();
                var rotation = result.comp2();
                var q = body.getPhysicsRotation(new Quaternion());
                rotation.set(new Quaterniond(q.getX(), q.getY(), q.getZ(), q.getW()));
            }
        }).safe(64, (framedDominion, id, e) -> {
            LOGGER.error("Error occured with the given system! [Id: " + id + "]", e);
        }));

//        logicScheduler.schedule(IdentifiedSystem.of(new Id("game", "test_grav"), dominion, dom -> {
//            for (var result : dom.findEntitiesWith(Named.class, Gravity.class)) {
//                var named = result.comp1();
//
//                if (!named.hasName() || !named.name().equals("test_grass")) return;
//
//                var gravity = result.comp2();
//
//                long window = Game.window.handle;
//
//                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
//                    gravity.y += 0.00001;
//                }
//
//                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
//                    gravity.y -= 0.00001;
//                }
//            }
//        }));

        var lockOut = new AtomicInteger();

        logicScheduler.schedule(IdentifiedSystem.of(new Id("game", "test_grav"), dominion, dom -> {
            if (lockOut.decrementAndGet() > 0) return;

            dom.findEntitiesWith(Named.class, PhysicsRigidBody.class).forAll((entity, named, body) -> {
                if (!named.hasName() || !named.name().equals("cow")) return;

                long window = Game.window.handle;

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
                    body.applyCentralForce(new Vector3f(0, -0.1f, 0));
                    lockOut.set(LOGIC_TICK_RATE / 4);
                }

                if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS) {
                    body.setPhysicsLocation(new Vector3f(0, 0, 0));
                    body.setLinearVelocity(new Vector3f(0, 0, 0));
                    body.setEnableSleep(false);
                    lockOut.set(LOGIC_TICK_RATE / 4);
                }
            });
        }));

        logicScheduler.tickAtFixedRate(LOGIC_TICK_RATE);

        loop(dominion);

        logicScheduler.shutDown();
        clientScheduler.shutDown();
        window.terminate();
    }

    private void loop(FramedDominion dom) {
        while (!window.shouldClose()) {
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