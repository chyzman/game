package com.chyzman.systems;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.freetype.FreeType;

public class Font {
    public Font(String font) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var freeType = stack.callocPointer(1);
            if (FreeType.FT_Init_FreeType(freeType) != 0) {
                throw new RuntimeException("ERROR::FREETYPE: Could not init FreeType Library");
            }

            if (FreeType.FT_New_Face(freeType.get(), font, 0, stack.callocPointer(1)) != 0) {
                throw new RuntimeException("ERROR::FREETYPE: Failed to load font");
            }
        }
    }
}
