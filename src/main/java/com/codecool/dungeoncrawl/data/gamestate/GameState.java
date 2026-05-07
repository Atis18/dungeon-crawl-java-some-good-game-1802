package com.codecool.dungeoncrawl.data.gamestate;

import java.util.List;

public class GameState {
    public final String map;
    public final int playerX, playerY, playerHealth;
    public final List<String> inventory;

    public GameState(String map, int playerX, int playerY, int playerHealth, List<String> inventory) {
        this.map = map;
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerHealth = playerHealth;
        this.inventory = inventory;
    }
}
