package com.chyzman.object.components;

import com.chyzman.Game;
import com.chyzman.gl.ElementMeshBuffer;
import com.chyzman.gl.VertexDescriptors;
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

public class MeshComponent<VF> {
    public final int texture;
    public final ElementMeshBuffer<VF> mesh;

    public MeshComponent(ElementMeshBuffer<VF> mesh, Id texture) {
        this.texture = Texture.load(texture);
        this.mesh = mesh;
    }

    public static MeshComponent<VertexDescriptors.PosColorTexNormalVertexFunction> obj(Id modelId, Id texture) {
        Obj model;

        try {
            model = ObjUtils.convertToRenderable(ObjReader.read(Files.newInputStream(modelId.toResourcePath("models"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var mesh = new ElementMeshBuffer<>(VertexDescriptors.POSITION_COLOR_TEXTURE_NORMAL, Renderer.POS_COLOR_TEXTURE_PROGRAM);
        int[] indices = ObjData.getFaceVertexIndicesArray(model);
        float[] vertices = ObjData.getVerticesArray(model);
        float[] texCoords = ObjData.getTexCoordsArray(model, 2, true);
        float[] normals = ObjData.getNormalsArray(model);

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

        return new MeshComponent<>(mesh, texture);
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
}
