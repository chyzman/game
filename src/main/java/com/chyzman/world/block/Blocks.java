package com.chyzman.world.block;

import com.chyzman.util.Id;

public class Blocks {
    private static int lastId = 0;
    public static final Block[] BLOCKS = new Block[256];
    public static final Block GRASS = new Block(new Id("game", "grass"));

    public static int register(Block block) {
        lastId++;
        int id = lastId;
        BLOCKS[id] = block;
        return id;
    }
}
