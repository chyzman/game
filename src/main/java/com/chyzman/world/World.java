package com.chyzman.world;

import com.chyzman.world.block.Blocks;
import com.chyzman.world.chunk.Chunk;
import com.chyzman.world.chunk.ChunkManager;
import com.chyzman.world.block.Block;

public class World {
    private final ChunkManager chunkManager = new ChunkManager();

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public Chunk getChunk(int x, int y, int z) {
        return chunkManager.getChunk(x, y, z);
    }

    public Block getBlock(int x, int y, int z) {
        return getChunk(x >> 4, y >> 4, z >> 4).getBlock(x & 15, y & 15, z & 15);
    }

    public void setBlock(int x, int y, int z, Block block) {
        getChunk(x >> 4, y >> 4, z >> 4).setBlock(x & 15, y & 15, z & 15, block);
    }
}
