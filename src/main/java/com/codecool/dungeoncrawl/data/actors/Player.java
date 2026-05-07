package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.Item;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {
    public static final String DEFAULT_NAME = "Player";
    private static final int DEFAULT_HEALTH = 20;
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
