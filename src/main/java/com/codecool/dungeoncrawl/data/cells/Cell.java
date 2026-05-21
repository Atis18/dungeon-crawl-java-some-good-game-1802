package com.codecool.dungeoncrawl.data.cells;

import com.codecool.dungeoncrawl.data.Drawable;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.actors.Actor;

public class Cell implements Drawable {
    private CellType type;
    private Actor actor;
    private Item item;
    private GameMap gameMap;
    private int x, y;

    public Cell(GameMap gameMap, int x, int y, CellType type) {
        this.gameMap = gameMap;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Cell getNeighbor(int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;
        
        if (newX < 0 || newX >= gameMap.getWidth() || newY < 0 || newY >= gameMap.getHeight()) {
            return this;
        }
        
        return gameMap.getCell(newX, newY);
    }

    @Override
    public String getTileName() {
        return type.getTileName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
