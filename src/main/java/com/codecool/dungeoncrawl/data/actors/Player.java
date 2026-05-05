package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.Cell;

public class Player extends Actor {
    public static final String DEFAULT_NAME = "Player";

    private String name;

    public Player(Cell cell) {
        this(cell, DEFAULT_NAME);
    }

    public Player(Cell cell, String name) {
        super(cell);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String trimmedName = name == null ? "" : name.trim();
        this.name = trimmedName.isEmpty() ? DEFAULT_NAME : trimmedName;
    }

    public String getTileName() {
        return "player";
    }
}
