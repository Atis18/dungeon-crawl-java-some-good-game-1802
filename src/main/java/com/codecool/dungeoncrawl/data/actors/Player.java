package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.items.Armor;
import com.codecool.dungeoncrawl.data.items.HealPotion;
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

    private boolean steppedOnDoorWithKey = false;

    private final List<Item> inventory = new ArrayList<>();

    private Armor equippedArmor = null;
    private Sword equippedSword = null;

    public void equipArmor(Armor armor) {
        equippedArmor = armor;
        setArmor(armor.getDefense());
    }

    public void equipSword(Sword sword) {
        equippedSword = sword;
        setAttackPower(DEFAULT_ATTACK_POWER + 1);
    }

    public Sword getEquippedSword() {
        return equippedSword;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

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

        if (nextCell == getCell()) {
            return;
        }

        if (nextCell.getType() == CellType.DOOR) {
            if (hasKey()) {
                removeKey();
                steppedOnDoorWithKey = true;
                super.move(dx, dy);
            }
            return;
        }

        if (nextCell.getType() == CellType.SAVE) {
            GameLogic.getLogic().saveGame();
            return;
        }

        if (nextCell.getType() == CellType.LOAD) {
            GameLogic.getLogic().loadGame();
            return;
        }

        if (nextCell.getItem() != null) {
            Item item = nextCell.getItem();
            if (item instanceof Armor) {
                equipArmor((Armor) item);
            } else if (item instanceof Sword) {
                equipSword((Sword) item);
            } else {
                inventory.add(item);
            }
            nextCell.setItem(null);
            if (nextCell.getType() == CellType.SWORD || nextCell.getType() == CellType.KEY || nextCell.getType() == CellType.HEALPOTION) {
                nextCell.setType(CellType.FLOOR);
            }
        }

        if (nextCell.getActor() != null) {
            attack(nextCell.getActor());
            return;
        }
        super.move(dx, dy);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void loadItemToInventory(String name) {
        switch (name.toLowerCase()) {
            case "sword":
                inventory.add(new Sword());
                break;
            case "key":
                inventory.add(new Key());
                break;
            case "healpotion":
                inventory.add(new HealPotion());
                break;
        }
    }

    public void heal() {

        Item potion = inventory.stream()
                .filter(item -> item.getName().equals("HealPotion"))
                .findFirst()
                .orElse(null);

        if (potion == null) {
            return;
        }

        setHealth(Math.min(getHealth() + 20, 100));

        inventory.remove(potion);
    }

    @Override
    public String getTileName() {
        return equippedArmor != null ? "player_armored" : "player";
    }

    public boolean steppedOnDoorWithKey() {
        return steppedOnDoorWithKey;
    }

    private boolean hasKey() {
        return inventory.stream()
                .anyMatch(item -> item instanceof Key);
    }

    private void removeKey() {

        Item key = inventory.stream()
                .filter(item -> item instanceof Key)
                .findFirst()
                .orElse(null);

        if (key != null) {
            inventory.remove(key);
        }
    }

    public void resetDoorFlag() {
        steppedOnDoorWithKey = false;
    }
}