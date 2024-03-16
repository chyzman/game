package com.chyzman.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class FreeTypeUtils {

    private static final Map<Integer, String> ft_errors = new HashMap<>();

    public static Optional<String> convertError(int errorCode){
        if(errorCode == 0) return Optional.empty();

        return Optional.of(ft_errors.getOrDefault(errorCode, "Unknown errorCode") + "[Code: " + errorCode + "]");
    }

    public static void throwIfError(int errorCode, Consumer<String> consumer){
        convertError(errorCode).ifPresent(consumer);
    }

    public static void throwIfError(int errorCode, String initialMessage){
        convertError(errorCode)
                .map(s -> "[ERROR::FREETYPE] " + initialMessage + ": ")
                .ifPresent(s -> { throw new IllegalStateException(s); });
    }

    static {
        ft_errors.put(0x00, "no error");

        ft_errors.put(0x01, "cannot open resource");
        ft_errors.put(0x02, "unknown file format");
        ft_errors.put(0x03, "broken file");
        ft_errors.put(0x04, "invalid FreeType version");
        ft_errors.put(0x05, "module version is too low");
        ft_errors.put(0x06, "invalid argument");
        ft_errors.put(0x07, "unimplemented feature");
        ft_errors.put(0x08, "broken table");
        ft_errors.put(0x09, "broken offset within table");
        ft_errors.put(0x0A, "array allocation size too large");
        ft_errors.put(0x0B, "missing module");
        ft_errors.put(0x0C, "missing property");

        // glyph/character errors
        ft_errors.put(0x10, "invalid glyph index");
        ft_errors.put(0x11, "invalid character code");
        ft_errors.put(0x12, "unsupported glyph image format");
        ft_errors.put(0x13, "cannot render this glyph format");
        ft_errors.put(0x14, "invalid outline");
        ft_errors.put(0x15, "invalid composite glyph");
        ft_errors.put(0x16, "too many hints");
        ft_errors.put(0x17, "invalid pixel size");
        ft_errors.put(0x18, "invalid SVG document");

        // handle errors
        ft_errors.put(0x20, "invalid object handle");
        ft_errors.put(0x21, "invalid library handle");
        ft_errors.put(0x22, "invalid module handle");
        ft_errors.put(0x23, "invalid face handle");
        ft_errors.put(0x24, "invalid size handle");
        ft_errors.put(0x25, "invalid glyph slot handle");
        ft_errors.put(0x26, "invalid charmap handle");
        ft_errors.put(0x27, "invalid cache manager handle");
        ft_errors.put(0x28, "invalid stream handle");

        // driver errors
        ft_errors.put(0x30, "too many modules");
        ft_errors.put(0x31, "too many extensions");

        // memory errors
        ft_errors.put(0x40, "out of memory");
        ft_errors.put(0x41, "unlisted object");

        // stream errors
        ft_errors.put(0x51, "cannot open stream");
        ft_errors.put(0x52, "invalid stream seek");
        ft_errors.put(0x53, "invalid stream skip");
        ft_errors.put(0x54, "invalid stream read");
        ft_errors.put(0x55, "invalid stream operation");
        ft_errors.put(0x56, "invalid frame operation");
        ft_errors.put(0x57, "nested frame access");
        ft_errors.put(0x58, "invalid frame read");

        // raster errors
        ft_errors.put(0x60, "raster uninitialized");
        ft_errors.put(0x61, "raster corrupted");
        ft_errors.put(0x62, "raster overflow");
        ft_errors.put(0x63, "negative height while rastering");

        // cache errors
        ft_errors.put(0x70, "too many registered caches");

        // TrueType and SFNT errors
        ft_errors.put(0x80, "invalid opcode");
        ft_errors.put(0x81, "too few arguments");
        ft_errors.put(0x82, "stack overflow");
        ft_errors.put(0x83, "code overflow");
        ft_errors.put(0x84, "bad argument");
        ft_errors.put(0x85, "division by zero");
        ft_errors.put(0x86, "invalid reference");
        ft_errors.put(0x87, "found debug opcode");
        ft_errors.put(0x88, "found ENDF opcode in execution stream");
        ft_errors.put(0x89, "nested DEFS");
        ft_errors.put(0x8A, "invalid code range");
        ft_errors.put(0x8B, "execution context too long");
        ft_errors.put(0x8C, "too many function definitions");
        ft_errors.put(0x8D, "too many instruction definitions");
        ft_errors.put(0x8E, "SFNT font table missing");
        ft_errors.put(0x8F, "horizontal header (hhea) table missing");
        ft_errors.put(0x90, "locations (loca) table missing");
        ft_errors.put(0x91, "name table missing");
        ft_errors.put(0x92, "character map (cmap) table missing");
        ft_errors.put(0x93, "horizontal metrics (hmtx) table missing");
        ft_errors.put(0x94, "PostScript (post) table missing");
        ft_errors.put(0x95, "invalid horizontal metrics");
        ft_errors.put(0x96, "invalid character map (cmap) format");
        ft_errors.put(0x97, "invalid ppem value");
        ft_errors.put(0x98, "invalid vertical metrics");
        ft_errors.put(0x99, "could not find context");
        ft_errors.put(0x9A, "invalid PostScript (post) table format");
        ft_errors.put(0x9B, "invalid PostScript (post) table");
        ft_errors.put(0x9C, "found FDEF or IDEF opcode in glyf bytecode");
        ft_errors.put(0x9D, "missing bitmap in strike");
        ft_errors.put(0x9E, "SVG hooks have not been set");

        // CFF, CID, and Type 1 errors
        ft_errors.put(0xA0, "opcode syntax error");
        ft_errors.put(0xA1, "argument stack underflow");
        ft_errors.put(0xA2, "ignore");
        ft_errors.put(0xA3, "no Unicode glyph name found");
        ft_errors.put(0xA4, "glyph too big for hinting");

        // BDF errors
        ft_errors.put(0xB0, "`STARTFONT' field missing");
        ft_errors.put(0xB1, "`FONT' field missing");
        ft_errors.put(0xB2, "`SIZE' field missing");
        ft_errors.put(0xB3, "`FONTBOUNDINGBOX' field missing");
        ft_errors.put(0xB4, "`CHARS' field missing");
        ft_errors.put(0xB5, "`STARTCHAR' field missing");
        ft_errors.put(0xB6, "`ENCODING' field missing");
        ft_errors.put(0xB7, "`BBX' field missing");
        ft_errors.put(0xB8, "`BBX' too big");
        ft_errors.put(0xB9, "Font header corrupted or missing fields");
        ft_errors.put(0xBA, "Font glyphs corrupted or missing fields");
    }
}
