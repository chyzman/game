package com.chyzman.gl;

import com.chyzman.Window;

import java.util.HashMap;
import java.util.Map;

public final class RenderContext {

    public final Window window;
    private final Map<String, GlProgram> programStore = new HashMap<>();

    public RenderContext(Window window, GlProgram... programs) {
        this.window = window;
        for (var program : programs) {
            if (this.programStore.containsKey(program.name)) {
                throw new IllegalArgumentException("Duplicate program name " + program.name);
            }

            this.programStore.put(program.name, program);
        }
    }

    public GlProgram findProgram(String name) {
        var program = this.programStore.get(name);
        if (program == null) throw new IllegalStateException("Missing required program " + name);

        return program;
    }
}
