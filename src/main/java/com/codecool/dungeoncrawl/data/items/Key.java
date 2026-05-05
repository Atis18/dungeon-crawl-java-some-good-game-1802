package com.codecool.dungeoncrawl.data.items;

import com.codecool.dungeoncrawl.data.Item;

public class Key implements Item {
    @Override
    public String getName() {
        return "Key";
    }

    @Override
    public String getTileName() {
        return "key";
    }
}
