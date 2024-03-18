package com.chyzman.world.block;

import com.chyzman.ui.core.Color;
import com.chyzman.util.Id;

public class Blocks {
    public static int lastId = 0;
    public static final Block[] BLOCKS = new Block[256];
    public static final Block GREEN = new Block(new Id("game", "grass"), Color.GREEN);
    public static final Block WHITE = new Block(new Id("game", "stone"), Color.WHITE);
    public static final Block BLUE = new Block(new Id("game", "blue"), Color.BLUE);
    public static final Block BLACK = new Block(new Id("game", "black"), Color.BLACK);
    public static final Block RED = new Block(new Id("game", "red"), Color.RED);

    public static int register(Block block) {
        lastId++;
        int id = lastId;
        BLOCKS[id] = block;
        return id;
    }
}
