package com.codecool.dungeoncrawl.data.items;

import com.codecool.dungeoncrawl.data.Item;

public class Armor implements Item {
    private static final int DEFENSE = 1;

    @Override
    public String getName() {
        return "Armor";
    }

    @Override
    public String getTileName() {
        return "armor";
    }

    public int getDefense() {
        return DEFENSE;
    }
}
