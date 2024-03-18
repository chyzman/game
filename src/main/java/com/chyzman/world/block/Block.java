package com.chyzman.world.block;

import com.chyzman.ui.core.Color;
import com.chyzman.util.Id;

public class Block {

    public final Id id;
    public final int lookupId;
    public final Color color;

    public Block(Id id, Color color) {
        this.id = id;
        this.lookupId = Blocks.register(this);
        this.color = color;
    }
}
