package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Actor;
import com.codecool.dungeoncrawl.data.actors.Skeleton;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private GameMap map;

    public GameLogic() {
        this.map = MapLoader.loadMap();
    }

    public GameLogic(GameMap map) {
        this.map = map;
    }

    public double getMapWidth() {
        return map.getWidth();
    }

    public double getMapHeight() {
        return map.getHeight();
    }

    public void setup() {
    }

    public Cell getCell(int x, int y) {
        return map.getCell(x, y);
    }

    public String getPlayerHealth() {
        return Integer.toString(map.getPlayer().getHealth());
    }

    public String getPlayerName() {
        return map.getPlayer().getName();
    }

    public void setPlayerName(String name) {
        map.getPlayer().setName(name);
    }

    public void fight(Actor attacker, Actor defender) {
        if (attacker == null || defender == null) {
            return;
        }

        attacker.attack(defender);
    }

    public void makeMonstersAct() {
        for (Skeleton skeleton : getMonsters()) {
            skeleton.takeTurn(map.getPlayer());
        }
    }

    public GameMap getMap() {
        return map;
    }

    private List<Skeleton> getMonsters() {
        List<Skeleton> monsters = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() instanceof Skeleton) {
                    monsters.add((Skeleton) cell.getActor());
                }
            }
        }

        return monsters;
    }
}
