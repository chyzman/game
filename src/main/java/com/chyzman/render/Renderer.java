package com.chyzman.render;

import com.chyzman.Game;
import com.chyzman.Window;
import com.chyzman.component.position.Position;
import com.chyzman.component.rotation.Rotation;
import com.chyzman.dominion.FramedDominion;
import com.chyzman.gl.*;
import com.chyzman.object.CameraConfiguration;
import com.chyzman.object.components.MeshComponent;
import com.chyzman.systems.TextRenderer;
import com.chyzman.ui.core.Color;
import com.chyzman.util.UtilUtil;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.objects.infos.CharacterController;
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
    public TextRenderer textRenderer;
    private float debugLineHeight = 0.0f;

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

        ElementMeshBuffer<VertexDescriptors.PosColorVertexFunction> debugBuffer = new ElementMeshBuffer<>(VertexDescriptors.POSITION_COLOR, POS_COLOR_PROGRAM);

        dominion.findEntitiesWith(PhysicsCharacter.class).forAll((entity, physicsRigidBody) -> {
            MatrixStack transform = Game.renderer.getModelMatrix();
            transform.push();
            var transformMatrix = physicsRigidBody.getTransform(null);
//            transform.translate(transformMatrix.getTranslation().x, transformMatrix.getTranslation().y, transformMatrix.getTranslation().z);
            transform.peek().rotate(new Quaternionf(transformMatrix.getRotation().getX(), transformMatrix.getRotation().getY(), transformMatrix.getRotation().getZ(), transformMatrix.getRotation().getW()));
            // TODO: convert this to a mesh component this code is very bad
            var box = physicsRigidBody.boundingBox(null);
            var low = new Vector3f(box.getMin(null).x, box.getMin(null).y, box.getMin(null).z);
            var high = new Vector3f(box.getMax(null).x, box.getMax(null).y, box.getMax(null).z);
            debugBuffer.builder.vertex(low.x,    low.y,  low.z); // 0
            debugBuffer.builder.vertex(high.x,   low.y,  low.z); // 1
            debugBuffer.builder.vertex(low.x,    high.y, low.z); // 2
            debugBuffer.builder.vertex(low.x,    low.y,  high.z); // 3

            debugBuffer.builder.vertex(high.x,   high.y, low.z); // 4
            debugBuffer.builder.vertex(high.x,   low.y,  high.z); // 5
            debugBuffer.builder.vertex(low.x,    high.y, high.z); // 6
            debugBuffer.builder.vertex(high.x,   high.y, high.z); // 7

            var indices = debugBuffer.getIndicesBuffer();
            // Bottom
            indices.int3(0, 1, 3);
            indices.int3(3, 1, 5);

            // Top
            indices.int3(7, 6, 4);
            indices.int3(4, 6, 2);

            // East
            indices.int3(0, 2, 3);
            indices.int3(3, 2, 6);

            // West
            indices.int3(7, 5, 4);
            indices.int3(4, 5, 1);

            // South
            indices.int3(0, 1, 2);
            indices.int3(2, 1, 4);

            indices.int3(7, 5, 6);
            indices.int3(6, 5, 3);
            debugBuffer.upload(false);
            debugBuffer.program.use();
            debugBuffer.program.uniformMat4("uProjection", getProjectionMatrix().peek());
            debugBuffer.program.uniformMat4("uView", getViewMatrix().peek());
            debugBuffer.program.uniformMat4("uModel", getModelMatrix().peek());
            debugBuffer.draw();

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
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        renderLine("FPS: " + fps, 2.0f, Color.WHITE);
        renderLine("", 2.0f, Color.WHITE);
        var cameraPosition = Game.camera.get(Position.class);
        renderLine("Pos:", 2.0f, Color.WHITE);
        renderLine("X: " + cameraPosition.x, 2.0f, Color.RED);
        renderLine("Y: " + cameraPosition.y, 2.0f, Color.GREEN);
        renderLine("Z: " + cameraPosition.z, 2.0f, Color.BLUE);
        renderLine("", 2.0f, Color.WHITE);
        var cameraRotation = Game.camera.get(Rotation.class);
        renderLine("Rot: (completely wrong ik)", 2.0f, Color.WHITE);
        float pitch = (float) Math.toDegrees(Math.atan2(2.0f * (cameraRotation.y * cameraRotation.z + cameraRotation.w * cameraRotation.x), cameraRotation.w * cameraRotation.w - cameraRotation.x * cameraRotation.x - cameraRotation.y * cameraRotation.y + cameraRotation.z * cameraRotation.z));
        renderLine("Pitch: " + pitch, 2.0f, Color.RED);
        float yaw = (float) Math.toDegrees(Math.asin(-2.0f * (cameraRotation.x * cameraRotation.z - cameraRotation.w * cameraRotation.y)));
        renderLine("Yaw: " + yaw, 2.0f, Color.GREEN);
        float roll = (float) Math.toDegrees(Math.atan2(2.0f * (cameraRotation.x * cameraRotation.y + cameraRotation.w * cameraRotation.z), cameraRotation.w * cameraRotation.w + cameraRotation.x * cameraRotation.x - cameraRotation.y * cameraRotation.y - cameraRotation.z * cameraRotation.z));
        renderLine("Roll: " + roll, 2.0f, Color.BLUE);

        debugLineHeight = 0.0f;
        if (polygonMode == GL_LINE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
    }


    private void renderLine(String text, float x, Color color) {
        debugLineHeight += 15;
        textRenderer.renderText(text, x, Game.window.height - debugLineHeight, 0.4f, color);
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