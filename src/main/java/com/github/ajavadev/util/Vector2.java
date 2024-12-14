package com.github.ajavadev.util;

import java.util.Objects;

/**
 * A 2D vector implementation specifically for pixel coordinates.
 * Uses integer precision to represent exact pixel positions.
 */
public class Vector2 {
    // X and Y pixel coordinates
    private int x;
    private int y;

    /**
     * Creates a vector at the origin (0, 0)
     */
    public Vector2() {
        this(0, 0);
    }

    /**
     * Creates a vector with specified x and y pixel coordinates.
     *
     * @param initialX x-coordinate in pixels
     * @param initialY y-coordinate in pixels
     */
    public Vector2(int initialX, int initialY) {
        this.x = initialX;
        this.y = initialY;
    }

    // Getters and Setters
    public void setX(int newX) {
        this.x = newX;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Sets both x and y coordinates in one method.
     *
     * @param newX new x-coordinate in pixels
     * @param newY new y-coordinate in pixels
     */
    public void set(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    /**
     * Calculate the manhattan distance between two points.
     * Useful for pixel-based distance calculations.
     *
     * @param other vector to calculate distance to
     * @return manhattan distance in pixels
     */
    public int manhattanDistance(Vector2 other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    /**
     * Calculate the Euclidean distance between two points.
     *
     * @param other vector to calculate distance to
     * @return distance in pixels
     */
    public double distance(Vector2 other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Add another vector to this vector.
     *
     * @param other vector to add
     * @return a new vector representing the sum
     */
    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtract another vector from this vector.
     *
     * @param other vector to subtract
     * @return a new vector representing the difference
     */
    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    /**
     * Translate the vector by specified x and y offsets.
     *
     * @param dx x-axis translation in pixels
     * @param dy y-axis translation in pixels
     * @return a new translated vector
     */
    public Vector2 translate(int dx, int dy) {
        return new Vector2(this.x + dx, this.y + dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2i = (Vector2) o;
        return x == vector2i.x && y == vector2i.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2i{x=" + x + ", y=" + y + '}';
    }
}