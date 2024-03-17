package com.chyzman.component.gameplay;

import com.chyzman.component.ClampedDouble;

public class Oxygen extends ClampedDouble {
    public Oxygen() {
        super(100.0, 0.0, 100.0);
    }
}