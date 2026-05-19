package com.codecool.dungeoncrawl.logic.map_generation;

public class RegionSpawn {

    public final int minX;
    public final int maxX;
    public final int minY;
    public final int maxY;

    public final char symbol;
    public final int amount;

    public RegionSpawn(int minX, int maxX,
                       int minY, int maxY,
                       char symbol,
                       int amount) {

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        this.symbol = symbol;
        this.amount = amount;
    }
}
