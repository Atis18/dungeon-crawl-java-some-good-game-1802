package com.codecool.dungeoncrawl.data.gamestate;

import java.util.List;

public class GameState {
    public final String map;
    public final String player_name;
    public final int playerX, playerY, playerHealth;
    public final List<String> inventory;

    public GameState(String map, String player_name, int playerX, int playerY, int playerHealth, List<String> inventory) {
        this.map = map;
        this.player_name = player_name;
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerHealth = playerHealth;
        this.inventory = inventory;
    }
}
