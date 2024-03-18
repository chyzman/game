package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.Window;
import com.chyzman.component.position.Position;
import com.chyzman.gl.GlProgram;
import com.chyzman.gl.GlShader;
import com.chyzman.gl.MatrixStack;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.components.CoolCube;
import com.chyzman.object.BasicObject;
import com.chyzman.systems.TextRenderer;
import dev.dominion.ecs.api.Dominion;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL45.*;

public class Renderer {
    public static final GlProgram POS_COLOR_PROGRAM;
    public static final GlProgram POS_COLOR_TEXTURE_PROGRAM;
    public static final GlProgram POS_COLOR_TEXTURE_NORMAL_PROGRAM;
    public static final GlProgram FONT_PROGRAM;
    private final List<RenderChunk> chunks = new ArrayList<>();
    private final MatrixStack projectionMatrix = new MatrixStack();
    private final MatrixStack transformMatrix = new MatrixStack();
    private final MatrixStack modelMatrix = new MatrixStack();
    public double deltaTime = 0.0f;	// Time between current frame and last frame
    public double lastFrame = 0.0f; // Time of last frame
    public double lastTime = 0.0f;
    private double framesPerSecond = 0.0f;
    public int fps;
    public static Vector3d cameraPosition = new Vector3d(0.0f, 0.0f, 0.0f);
    public TextRenderer textRenderer = new TextRenderer();

    public Renderer(Window window, Dominion dominion) {
        for (var entityResult : dominion.findEntitiesWith(Position.class, CameraConfiguration.class)) {
            CameraConfiguration camera = entityResult.comp2();

            //Game.renderer.getProjectionMatrix().ortho2D(-this.width/2f, this.width/2f, -this.height/2f, this.height/2f);
            getProjectionMatrix().peek().perspective((float) Math.toRadians(camera.fov), (float) window.width / (float) window.height, 0.1f, 1000f);
        }
        Game.GAME.world.getChunkManager().getChunks().forEach((chunkPos, chunk) -> {
            chunks.add(new RenderChunk(chunk));
        });
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void update(Dominion dominion) {
        double currentFrame = GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        for (RenderChunk chunk : chunks)
            chunk.draw();

        for(var gameObject : Game.GAME.gameObjects) {
            gameObject.update();
        }

        for (var entity : dominion.findEntitiesWith(CoolCube.class, Position.class)) {
            var cube = entity.comp1();
            var pos = entity.comp2();
            var window = Game.window;
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, cube.chyz);
            cube.mesh.program.use();
            cube.mesh.program.uniformMat4("uProjection", getProjectionMatrix().peek());
            cube.mesh.program.uniformMat4("uView", getViewMatrix().peek());

            cube.mesh.program.uniformMat4("uModel", new Matrix4f(getModelMatrix().peek()).translate((float) pos.x, (float) pos.y, (float) pos.z));
            cube.mesh.draw();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        for (var resultEntity : dominion.findEntitiesWith(Position.class, BasicObject.class)) {
            resultEntity.comp2().draw(resultEntity.comp1());
        }

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
                    "position_color",
                    GlShader.vertex(Files.newInputStream(Path.of("src/main/resources/shaders/pos_color.vert"))),
                    GlShader.fragment(Files.newInputStream(Path.of("src/main/resources/shaders/pos_color.frag")))
            );
            POS_COLOR_TEXTURE_PROGRAM = new GlProgram(
                    "position",
                    GlShader.vertex(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.vert"))),
                    GlShader.fragment(Files.newInputStream(Path.of("src/main/resources/shaders/Textured.frag")))
            );
            POS_COLOR_TEXTURE_NORMAL_PROGRAM = new GlProgram(
                    "position_color_tex_normal",
                    GlShader.vertex(Files.newInputStream(Path.of("src/main/resources/shaders/pos_color_texture_normal.vert"))),
                    GlShader.fragment(Files.newInputStream(Path.of("src/main/resources/shaders/pos_color_texture_normal.frag")))
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
}