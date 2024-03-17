package com.chyzman.world.chunk;

import com.chyzman.util.NibbleArray;
import com.chyzman.world.block.Block;
import com.chyzman.world.block.Blocks;

public class Chunk {
    public static final int CHUNK_SIZE = 16;
    public final int x, y, z;
    private final NibbleArray blocks = new NibbleArray(8, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE); // TODO: use nibble array for now

    public Chunk(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getIndex(int x, int y, int z) {
        return y << 8 | x << 4 | z;
    }

    public void setBlock(int x, int y, int z, Block block) {
        blocks.set(getIndex(x, y, z), block.lookupId);
    }

    public Block getBlock(int x, int y, int z) {
        return Blocks.BLOCKS[blocks.get(getIndex(x, y, z))];
    }
}
