package com.codecool.dungeoncrawl.data.items;

import com.codecool.dungeoncrawl.data.Item;

public class Sword implements Item {
    @Override
    public String getName() {
        return "Sword";
    }

    @Override
    public String getTileName() {
        return "sword";
    }
}
