package com.chyzman.systems;

import com.chyzman.util.NibbleArray;

public class Chunk {
    public static final int CHUNK_SIZE = 32;
    public final int x, y, z;
    private final NibbleArray blocks = new NibbleArray(16, CHUNK_SIZE * 3); // TODO: use nibble array for now

    public Chunk(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
