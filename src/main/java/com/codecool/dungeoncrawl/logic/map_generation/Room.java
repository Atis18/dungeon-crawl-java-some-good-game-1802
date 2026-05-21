package com.codecool.dungeoncrawl.logic.map_generation;

public class Room {

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int centerX() {
        return x + width / 2;
    }

    public int centerY() {
        return y + height / 2;
    }

    public boolean intersects(Room other) {

        return x <= other.x + other.width + 1 &&
                x + width + 1 >= other.x &&
                y <= other.y + other.height + 1 &&
                y + height + 1 >= other.y;
    }
}