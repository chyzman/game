package com.chyzman.component;

import org.joml.Quaterniond;

public class Rotation {
    Quaterniond rot = new Quaterniond();

    public Rotation() {
    }

    public Rotation(Quaterniond rot) {
        this.rot = rot;
    }
}