package com.chyzman.world;

import com.chyzman.world.block.Block;
import com.chyzman.world.chunk.Chunk;
import com.chyzman.world.chunk.ChunkManager;
import dev.dominion.ecs.api.Dominion;

public class World {
    private final ChunkManager chunkManager;
    private final Dominion dominion;

    public World(Dominion dominion) {
        this.dominion = dominion;
        this.chunkManager = new ChunkManager();
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public Chunk getChunk(int x, int y, int z) {
        return chunkManager.getChunk(x, y, z);
    }

    public Block getBlock(int x, int y, int z) {
        var chunk = getChunk(x >> 4, y >> 4, z >> 4);
        return chunk == null ? null : chunk.getBlock(x & Chunk.CHUNK_MASK, y & Chunk.CHUNK_MASK, z & Chunk.CHUNK_MASK);
    }

    public void setBlock(int x, int y, int z, Block block) {
        chunkManager.getOrLoadChunk(x >> 4, y >> 4, z >> 4).setBlock(x & Chunk.CHUNK_MASK, y & Chunk.CHUNK_MASK, z & Chunk.CHUNK_MASK, block);
    }

    public Dominion getDominion() {
        return dominion;
    }
}