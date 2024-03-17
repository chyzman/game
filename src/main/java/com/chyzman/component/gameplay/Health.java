package com.chyzman.component.gameplay;

import com.chyzman.component.ClampedDouble;

public class Health extends ClampedDouble {
    public Health() {
        super();
        this.min(0.0).max(100.0);
    }
}