package com.chyzman.systems;

import com.chyzman.util.FreeTypeUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.freetype.FreeType;

public class Font {

    public Font(String font) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var freeType = stack.callocPointer(1);

            FreeTypeUtils.throwIfError(FreeType.FT_Init_FreeType(freeType), "Could not init FreeType Library");

            FreeTypeUtils.throwIfError(FreeType.FT_New_Face(freeType.get(), "src/main/resources/fonts/" + font, 0, stack.callocPointer(1)), "Failed to load font");
        }
    }

    //--
}
