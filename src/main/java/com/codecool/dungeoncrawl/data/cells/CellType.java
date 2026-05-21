package com.codecool.dungeoncrawl.data.cells;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.*;
import com.codecool.dungeoncrawl.data.items.*;

public enum CellType {

    EMPTY(' ', "empty", (cell, map) -> {
    }),
    FLOOR('.', "floor", (cell, map) -> {
    }),
    WALL('#', "wall", (cell, map) -> {
    }),
    SAVE('f', "save", (cell, map) -> {
    }),
    LOAD('l', "load", (cell, map) -> {
    }),

    PLAYER('@', "floor", (cell, map) -> {
        map.setPlayer(new Player(cell));
    }),

    SKELETON('s', "floor", (cell, map) -> {
        new Skeleton(cell);
    }),

    KEY('k', "key", (cell, map) -> {
        cell.setItem(new Key());
    }),

    SWORD('w', "sword", (cell, map) -> {
        cell.setItem(new Sword());
    }),

    DOOR('d', "door", (cell, map) ->{

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