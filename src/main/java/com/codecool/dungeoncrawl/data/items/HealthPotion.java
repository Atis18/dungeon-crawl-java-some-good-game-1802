package com.codecool.dungeoncrawl.data.items;

import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.actors.Player;

public class HealthPotion implements Item {
    private static final int HEAL_AMOUNT = 20;

    @Override
    public String getName() {
        return "Health Potion";
    }

    @Override
    public String getTileName() {
        return "health_potion";
    }

    public void use(Player player) {
        player.setHealth(player.getHealth() + HEAL_AMOUNT);
    }
}
