package com.chyzman.component.gameplay;

import com.chyzman.component.ClampedDouble;

public class Health extends ClampedDouble {
    public Health() {
        super(100.0, 0.0, 100.0);
    }
}