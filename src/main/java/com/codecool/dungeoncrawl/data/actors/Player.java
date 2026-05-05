package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.Item;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {
    private List<Item> inventory = new ArrayList<>();

    public Player(Cell cell) {
        super(cell);
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if (nextCell.getItem() != null) {
            inventory.add(nextCell.getItem());
            nextCell.setItem(null);
        }
        super.move(dx, dy);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    @Override
    public String getTileName() {
        return "player";
    }
}
