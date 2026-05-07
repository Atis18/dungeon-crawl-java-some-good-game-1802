package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.CellType;
import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.items.Key;
import com.codecool.dungeoncrawl.data.items.Sword;
import com.codecool.dungeoncrawl.logic.GameLogic;

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

        if (nextCell.getType() == CellType.SAVE) {
            GameLogic.getLogic().saveGame();
            return;
        }

        if (nextCell.getType() == CellType.LOAD) {
            GameLogic.getLogic().loadGame();
            return;
        }

        if (nextCell.getItem() != null) {
            inventory.add(nextCell.getItem());
            nextCell.setItem(null);
            if (nextCell.getType() == CellType.SWORD || nextCell.getType() == CellType.KEY) {
                nextCell.setType(CellType.FLOOR);
            }
        }
        super.move(dx, dy);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void loadItemToInventory(String name){
        switch (name.toLowerCase()){
            case "sword":
                inventory.add(new Sword());
                break;
            case "key":
                inventory.add(new Key());
                break;
        }
    }

    @Override
    public String getTileName() {
        return "player";
    }
}
