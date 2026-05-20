package com.codecool.dungeoncrawl.data.items;

import com.codecool.dungeoncrawl.data.Item;

public class HealPotion implements Item {

    @Override
    public String getName() {
        return "HealPotion";
    }

    @Override
    public String getTileName() {
        return "healpotion";
    }
}
