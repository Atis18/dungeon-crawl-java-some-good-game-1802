package com.codecool.dungeoncrawl.data;

public enum MapObjectType {
    EMPTY(' '),
    WALL('#'),
    FLOOR('.'),

    PLAYER('@'),
    SKELETON('s'),

    SAVE('f'),
    LOAD('l'),

    KEY('k'),
    SWORD('w');

    private final char symbol;

    MapObjectType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static MapObjectType fromSymbol(char c) {

        for (MapObjectType type : values()) {
            if (type.symbol == c) {
                return type;
            }
        }

        throw new RuntimeException(
                "Unknown symbol: " + c
        );
    }
}
