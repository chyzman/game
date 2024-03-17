package com.chyzman.world.block;

import com.chyzman.util.Id;

public class Block {

    public final Id id;
    public final int lookupId;

    public Block(Id id) {
        this.id = id;
        this.lookupId = Blocks.register(this);
    }
}
