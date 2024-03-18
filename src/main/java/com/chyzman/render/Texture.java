package com.chyzman.render;

import com.chyzman.util.Id;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL45.*;

public class Texture {

    private static final HashMap<Id, Integer> idToTexture = new HashMap<>();

    // TODO(glisco) clean up this mess
    public static int load(Id textureId) {
        if (idToTexture.containsKey(textureId)) {
            return idToTexture.get(textureId);
        }

        int width;
        int height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buffer = STBImage.stbi_load(textureId.toResourcePath("texture").toAbsolutePath().toString(), w, h, channels, 4);
            if (buffer == null) {
                throw new Exception("Can't load file " + textureId + " " + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            int id = glGenTextures();
            idToTexture.put(textureId, id);
            glBindTexture(GL_TEXTURE_2D, id);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(buffer);
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Missing texture " + textureId, e);
        }
    }

}