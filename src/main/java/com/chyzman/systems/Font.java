package com.chyzman.systems;

import com.chyzman.util.FreeTypeUtils;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.freetype.FT_Face;
import org.lwjgl.util.freetype.FreeType;

import java.util.HashMap;
import java.util.Map;

public class Font {
    private final FT_Face face;
    public final Map<Character, FontCharacter> characters = new HashMap<>();

    public Font(String font) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var freeType = stack.callocPointer(1);
            var facePtr = stack.callocPointer(1);

            FreeTypeUtils.throwIfError(FreeType.FT_Init_FreeType(freeType), "Could not init FreeType Library");

            FreeTypeUtils.throwIfError(FreeType.FT_New_Face(freeType.get(), "src/main/resources/fonts/" + font, 0, facePtr), "Failed to load font");

            face = FT_Face.create(facePtr.get());
            FreeType.FT_Set_Pixel_Sizes(face, 0, 48);
        }
        load();
    }

    public void load() {
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1); // disable byte-alignment restriction

        for (char c = 0; c < 128; c++) {
            // load character glyph
            if (FreeType.FT_Load_Char(face, c, FreeType.FT_LOAD_RENDER) != 0) {
                System.err.println("ERROR::FREETYTPE: Failed to load Glyph");
                continue;
            }
            // generate texture
            int texture = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RED,
                    face.glyph().bitmap().width(),
                    face.glyph().bitmap().rows(),
                    0,
                    GL11.GL_RED,
                    GL11.GL_UNSIGNED_BYTE,
                    face.glyph().bitmap().buffer(128)
            );
            // set texture options
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            // now store character for later use
            FontCharacter character = new FontCharacter(
                    texture,
                    new Vector2i(face.glyph().bitmap().width(), face.glyph().bitmap().rows()),
                    new Vector2i(face.glyph().bitmap_left(), face.glyph().bitmap_top()),
                    face.glyph().advance().x());
            characters.put(c, character);
        }
    }

    public record FontCharacter(int textureId, Vector2i size, Vector2i bearing, long advance) {
    }
}
