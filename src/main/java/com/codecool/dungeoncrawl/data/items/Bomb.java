package com.codecool.dungeoncrawl.data.items;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.actors.Actor;
import com.codecool.dungeoncrawl.data.actors.Player;

public class Bomb implements Item {
    private static final int DAMAGE = 50;
    private static final int RANGE = 3;

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public String getTileName() {
        return "bomb";
    }

    public void use(Player player) {
        Cell playerCell = player.getCell();
        for (int dx = -RANGE; dx <= RANGE; dx++) {
            for (int dy = -RANGE; dy <= RANGE; dy++) {
                Cell cell = playerCell.getNeighbor(dx, dy);
                if (cell == null) continue;
                Actor actor = cell.getActor();
                if (actor != null && actor != player) {
                    actor.takeDamage(DAMAGE);
                }
            }
        }
    }
}
