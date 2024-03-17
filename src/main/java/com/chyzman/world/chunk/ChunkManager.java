package com.chyzman.world.chunk;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

public class ChunkManager {
    private final Object2ObjectMap<ChunkPos, Chunk> chunks = new Object2ObjectLinkedOpenHashMap<>();
    public Chunk getChunk(int x, int y, int z) {
        return chunks.computeIfAbsent(new ChunkPos(x, y, z), chunkPos -> new Chunk(x, y, z));
    }

    public Object2ObjectMap<ChunkPos, Chunk> getChunks() {
        return chunks;
    }

    public int getLoadedChunks() {
        return chunks.size();
    }
}
