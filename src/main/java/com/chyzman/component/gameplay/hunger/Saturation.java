package com.chyzman.component.gameplay.hunger;

import com.chyzman.component.ClampedDouble;

public class Saturation extends ClampedDouble {
    public Saturation() {
        super(100.0, 0.0, 100.0);
    }
}