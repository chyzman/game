package com.chyzman.component;

public class Health {
    private double health;
    private double maxHealth;

    public Health() {
    }

    public Health(double health, double maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public double health() {
        return health;
    }

    public Health health(double health) {
        this.health = health;
        return this;
    }

    public double maxHealth() {
        return maxHealth;
    }

    public Health maxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }
}