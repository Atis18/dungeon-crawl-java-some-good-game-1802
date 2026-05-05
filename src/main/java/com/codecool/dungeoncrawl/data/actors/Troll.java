package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.Cell;

public class Troll extends Actor {
    public Troll(Cell cell) {
        super(cell);
        setHealth(20);
    }

    @Override
    public String getTileName() {
        return "troll";
    }
}