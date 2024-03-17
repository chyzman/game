package com.chyzman.component;

import com.chyzman.util.Mth;

public class ClampedDouble {
    private Double value;
    private Double min;
    private Double max;

    public ClampedDouble() {
    }

    public ClampedDouble(Double value, Double min, Double max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public Double value() {
        return value;
    }

    public ClampedDouble value(Double value) {
        this.value = Mth.clamp(value, min, max);
        return this;
    }

    public Double min() {
        return min;
    }

    public ClampedDouble min(Double min) {
        this.min = min;
        this.value(value);
        return this;
    }

    public Double max() {
        return max;
    }

    public ClampedDouble max(Double max) {
        this.max = max;
        this.value(value);
        return this;
    }
}