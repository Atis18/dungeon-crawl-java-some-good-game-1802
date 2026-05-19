package com.codecool.dungeoncrawl.logic.map_generation;

public class FixedSpawn {

    public final int x;
    public final int y;
    public final char symbol;

    public FixedSpawn(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }
}