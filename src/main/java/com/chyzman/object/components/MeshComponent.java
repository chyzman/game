package com.chyzman.object.components;

import com.chyzman.Game;
import com.chyzman.gl.ElementMeshBuffer;
import com.chyzman.gl.VertexDescriptors;
import com.chyzman.object.GameObject;
import com.chyzman.render.Renderer;
import com.chyzman.render.Texture;
import com.chyzman.ui.core.Color;
import com.chyzman.util.Id;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MeshComponent extends GameObject {
    public final int texture;
    public final ElementMeshBuffer<VertexDescriptors.PosColorTexNormalVertexFunction> mesh;

    public MeshComponent(String obj, Id texture) {
        this.texture = Texture.load(texture);
        Obj chyzModel;

        try {
            chyzModel = ObjUtils.convertToRenderable(ObjReader.read(Files.newInputStream(Path.of("src/main/resources/models/" + obj + ".obj"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.mesh = new ElementMeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE_NORMAL, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        int[] indices = ObjData.getFaceVertexIndicesArray(chyzModel);
        float[] vertices = ObjData.getVerticesArray(chyzModel);
        float[] texCoords = ObjData.getTexCoordsArray(chyzModel, 2, true);
        float[] normals = ObjData.getNormalsArray(chyzModel);

        for (int vtx = 0; vtx < vertices.length / 3; vtx++) {
            mesh.builder.vertex(
                    vertices[vtx * 3], vertices[vtx * 3 + 1], vertices[vtx * 3 + 2],
                    Color.WHITE,
                    texCoords[vtx * 2],
                    texCoords[vtx * 2 + 1],
                    normals[vtx * 3], normals[vtx * 3 + 1], normals[vtx * 3 + 2]
            );
        }

        var indexBuffer = mesh.getIndicesBuffer();
        for (int idx = 0; idx < indices.length / 3; idx++) {
            indexBuffer.int3(
                    indices[idx * 3],
                    indices[idx * 3 + 1],
                    indices[idx * 3 + 2]
            );
        }

        mesh.upload(false);
    }

    public ElementMeshBuffer<VertexDescriptors.PosColorTexNormalVertexFunction> getMesh() {
        return mesh;
    }

    public void render() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        var renderer = Game.renderer;
        mesh.program.use();
        mesh.program.uniformMat4("uProjection", renderer.getProjectionMatrix().peek());
        mesh.program.uniformMat4("uView", renderer.getViewMatrix().peek());
        mesh.program.uniformMat4("uModel", renderer.getModelMatrix().peek());
        mesh.program.uniformSampler("textureSampler", this.texture, 0);
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void update() {

    }
}
