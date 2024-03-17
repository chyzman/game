package com.chyzman.component.gameplay.hunger;

import com.chyzman.component.ClampedDouble;

public class Hunger extends ClampedDouble {
    public Hunger() {
        super(100.0, 0.0, 100.0);
    }
}