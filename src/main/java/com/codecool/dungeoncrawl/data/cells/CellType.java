package com.codecool.dungeoncrawl.data.cells;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.*;
import com.codecool.dungeoncrawl.data.items.*;

public enum CellType {

    EMPTY(' ', "empty", (cell, map) -> {
    }),
    WALL('#', "wall", (cell, map) -> {
    }),
    FLOOR('.', "floor", (cell, map) -> {
    }),
    PLAYER('@', "floor", (cell, map) -> {
        map.setPlayer(new Player(cell));
    }),
    SAVE('f', "save", (cell, map) -> {
    }),
    LOAD('l', "load", (cell, map) -> {
    }),
    HEALPOTION('h', "healpotion", (cell, map) -> {
        cell.setItem(new HealPotion());
    }),
    SWORD('w', "sword", (cell, map) -> {
        cell.setItem(new Sword());
    }),
    KEY('k', "key", (cell, map) -> {
        cell.setItem(new Key());
    }),
    BOMB('b', "bomb", (cell, map) -> {
        cell.setItem(new Bomb());
    }),
    ARMOR('a', "armor", (cell, map) -> {
        cell.setItem(new Armor());
    }),

    DOOR('d', "door", (cell, map) -> {
    });

    private final char symbol;
    private final String tileName;

    private final CellInitializer initializer;

    CellType(char symbol,
             String tileName,
             CellInitializer initializer) {

        this.symbol = symbol;
        this.tileName = tileName;
        this.initializer = initializer;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getTileName() {
        return tileName;
    }

    public void apply(Cell cell, GameMap map) {

        // always set the cell type itself
        cell.setType(this);

        // then run custom logic
        initializer.initialize(cell, map);
    }

    public static CellType fromSymbol(char symbol) {

        for (CellType type : values()) {
            if (type.symbol == symbol) {
                return type;
            }
        }

        throw new RuntimeException(
                "Unknown symbol: " + symbol
        );
    }
}