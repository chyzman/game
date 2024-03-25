package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.Window;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.gl.GlProgram;
import com.chyzman.gl.GlShader;
import com.chyzman.gl.MatrixStack;
import com.chyzman.gl.RenderContext;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.components.MeshComponent;
import com.chyzman.systems.TextRenderer;
import com.chyzman.util.UtilUtil;
import com.jme3.bullet.objects.PhysicsRigidBody;
import dev.dominion.ecs.api.Dominion;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL45.*;

public class Renderer {
    public static final GlProgram POS_COLOR_PROGRAM;
    public static final GlProgram POS_COLOR_TEXTURE_PROGRAM;
    public static final GlProgram POS_COLOR_TEXTURE_NORMAL_PROGRAM;
    public static final GlProgram FONT_PROGRAM;
    private final RenderContext context;
    private final List<RenderChunk> chunks = new ArrayList<>();
    private final MatrixStack projectionMatrix = new MatrixStack();
    private final MatrixStack transformMatrix = new MatrixStack();
    private final MatrixStack modelMatrix = new MatrixStack();
    public double deltaTime = 0.0f;    // Time between current frame and last frame
    public double lastFrame = 0.0f; // Time of last frame
    public double lastTime = 0.0f;
    private double framesPerSecond = 0.0f;
    public int fps;
    public static Vector3d cameraPosition = new Vector3d(0.0f, 0.0f, 0.0f);
    public TextRenderer textRenderer;

    public Renderer(Window window, Dominion dominion) {
        this.context = new RenderContext(window, POS_COLOR_PROGRAM, POS_COLOR_TEXTURE_PROGRAM, POS_COLOR_TEXTURE_NORMAL_PROGRAM, FONT_PROGRAM);
        this.textRenderer = new TextRenderer(context);
        for (var entityResult : dominion.findEntitiesWith(Position.class, CameraConfiguration.class)) {
            CameraConfiguration camera = entityResult.comp2();

            //Game.renderer.getProjectionMatrix().ortho2D(-this.width/2f, this.width/2f, -this.height/2f, this.height/2f);
            getProjectionMatrix().peek().perspective((float) Math.toRadians(camera.fov), (float) window.width / (float) window.height, 0.1f, 1000f);
        }
        Game.GAME.world.getChunkManager().getChunks().forEach((chunkPos, chunk) -> {
            chunks.add(new RenderChunk(chunk, context));
        });
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void update(FramedDominion dominion) {
        double currentFrame = GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        for (RenderChunk chunk : chunks)
            chunk.draw();

        dominion.findEntitiesWith(
                MeshComponent.class
        ).forAll((entity, meshComponent) -> {
            Vector3d pos;
            Quaternionf rotation;
            if (entity.has(PhysicsRigidBody.class)) {
                var body = entity.get(PhysicsRigidBody.class);
                var transform = body.getTransform(null);
                pos = new Vector3d(
                        transform.getTranslation().x,
                        transform.getTranslation().y,
                        transform.getTranslation().z
                );
                rotation = new Quaternionf(
                        transform.getRotation().getX(),
                        transform.getRotation().getY(),
                        transform.getRotation().getZ(),
                        transform.getRotation().getW()
                );
            } else {
                pos = UtilUtil.thisOr(entity.get(Position.class), new Vector3d());
                rotation = UtilUtil.thisOr(entity.get(Rotation.class), new Quaternionf());
            }
            MatrixStack transform = Game.renderer.getModelMatrix();
            transform.push();
            transform.translate((float) pos.x, (float) pos.y, (float) pos.z);
            transform.peek().rotate(rotation);
            transform.translate(0, -1f, 0);
            meshComponent.render();
            transform.pop();
        });

//        for (var entity : dominion.findEntitiesWith(CoolCube.class, Position.class)) {
//            var cube = entity.comp1();
//            var pos = entity.comp2();
//            var window = Game.window;
//            GL11.glEnable(GL11.GL_DEPTH_TEST);
//            GL11.glBindTexture(GL11.GL_TEXTURE_2D, cube.chyz);
//            cube.mesh.program.use();
//            cube.mesh.program.uniformMat4("uProjection", getProjectionMatrix().peek());
//            cube.mesh.program.uniformMat4("uView", getViewMatrix().peek());
//
//            cube.mesh.program.uniformMat4("uModel", new Matrix4f(getModelMatrix().peek()).translate((float) pos.x, (float) pos.y, (float) pos.z));
//            cube.mesh.draw();
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
//        }

//        for (var resultEntity : dominion.findEntitiesWith(Position.class, BasicObject.class)) {
//            resultEntity.comp2().draw(resultEntity.comp1());
//        }

        ++framesPerSecond;
        if (currentFrame - lastTime > 1.0f) {
            lastTime = currentFrame;
            fps = (int) framesPerSecond;
            framesPerSecond = 0;
        }

        renderDebug();

    }

    public void renderDebug() {
        var polygonMode = glGetInteger(GL_POLYGON_MODE);
        if (polygonMode == GL_LINE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        textRenderer.renderText("FPS: " + fps, 2.0f, 3.0f, 0.25f, new Vector3f(1f, 1f, 1f));
        textRenderer.renderText("Pos: (" + cameraPosition.x + ", " + cameraPosition.y + ", " + cameraPosition.z + ")", 2.0f, 26.0f, 0.25f, new Vector3f(1f, 1f, 1f));
        textRenderer.renderText("Loaded Chunks: " + Game.GAME.world.getChunkManager().getLoadedChunks(), 2.0f, 49.0f, 0.25f, new Vector3f(1f, 1f, 1f));
        if (polygonMode == GL_LINE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
    }

    static {
        try {
            POS_COLOR_PROGRAM = new GlProgram(
                    "pos_color",
                    GlShader.vertex("pos_color.vert"),
                    GlShader.fragment("pos_color.frag")
            );
            POS_COLOR_TEXTURE_PROGRAM = new GlProgram(
                    "pos_color_tex",
                    GlShader.vertex("pos_color_tex.vert"),
                    GlShader.fragment("pos_color_tex.frag")
            );
            POS_COLOR_TEXTURE_NORMAL_PROGRAM = new GlProgram(
                    "pos_color_tex_normal",
                    GlShader.vertex("pos_color_tex_normal.vert"),
                    GlShader.fragment("pos_color_tex_normal.frag")
            );
            FONT_PROGRAM = new GlProgram(
                    "font",
                    GlShader.vertex("font.vert"),
                    GlShader.fragment("font.frag")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MatrixStack getProjectionMatrix() {
        return projectionMatrix;
    }

    public MatrixStack getViewMatrix() {
        return transformMatrix;
    }

    public MatrixStack getModelMatrix() {
        return modelMatrix;
    }

    public RenderContext getContext() {
        return context;
    }
}