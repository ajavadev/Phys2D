package com.github.ajavadev.entities;

public class Shape {
    private final ShapeType type;

    public Shape(ShapeType type) {
        this.type = type;
    }

    public ShapeType getType() {
        return type;
    }
}