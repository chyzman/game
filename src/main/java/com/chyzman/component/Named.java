package com.chyzman.component;

public record Named(String name) {

    public static Named empty() {
        return new Named("");
    }

    public boolean hasName(){
        return this.name().isEmpty();
    }
}
