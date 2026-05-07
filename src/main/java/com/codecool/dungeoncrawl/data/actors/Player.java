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
    private static final String DEFAULT_NAME = "Player";
    private static final int DEFAULT_HEALTH = 100;
    private static final int DEFAULT_ATTACK_POWER = 5;
    private static final int DEFAULT_ATTACK_RANGE = 1;

    private String name;

    private List<Item> inventory = new ArrayList<>();

    public Player(Cell cell) {
        this(cell, DEFAULT_NAME);
    }

    public Player(Cell cell, String name) {
        super(cell);
        setHealth(DEFAULT_HEALTH);
        setAttackPower(DEFAULT_ATTACK_POWER);
        setAttackRange(DEFAULT_ATTACK_RANGE);
        setName(name);
    }

    public static String getDefaultName() {
        return DEFAULT_NAME;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String trimmedName = name == null ? "" : name.trim();
        this.name = trimmedName.isEmpty() ? DEFAULT_NAME : trimmedName;
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

        if (nextCell.getActor() != null) {
            GameLogic.getLogic().fight(this, nextCell.getActor());
            return;
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