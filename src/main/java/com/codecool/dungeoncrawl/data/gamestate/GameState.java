package com.codecool.dungeoncrawl.data.gamestate;

public class GameState {
    public final String map;
    public final int playerX, playerY, playerHealth;

    public GameState(String map, int playerX, int playerY, int playerHealth) {
        this.map = map;
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerHealth = playerHealth;
    }
}
