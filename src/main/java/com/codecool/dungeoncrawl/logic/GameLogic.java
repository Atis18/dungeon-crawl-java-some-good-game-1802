package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.actors.Actor;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.gamestate.GameState;
import com.codecool.dungeoncrawl.data.gamestate.GameStateHandler;
import com.codecool.dungeoncrawl.logic.map_generation.FixedSpawn;
import com.codecool.dungeoncrawl.logic.map_generation.MapGenerator;
import com.codecool.dungeoncrawl.logic.map_generation.RegionSpawn;
import com.codecool.dungeoncrawl.logic.map_generation.Room;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private static GameLogic logic;
    private GameMap map;

    private MapGenerator mapGenerator;

    public GameLogic() {
        logic = this;

        this.mapGenerator = new MapGenerator(40, 30);
        this.map = mapGenerator.generate();
    }

    private static int randomInRange(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    public void nextMap() {

        // regenerate map (slightly harder each level)
        int width = randomInRange(50, 120);
        int height = randomInRange(35, 60);

        this.mapGenerator = new MapGenerator(width, height);
        this.map = mapGenerator.generate();

        resetPlayer();
    }

    private void resetPlayer() {

        Player player = map.getPlayer();

        // full heal
        player.setHealth(100);

        // move player to start room (same logic as generator)
        Room startRoom = mapGenerator.getStartRoom(); // we assume you expose it

        int x = startRoom.centerX();
        int y = startRoom.centerY();

        Cell oldCell = player.getCell();
        if (oldCell != null) {
            oldCell.setActor(null);
        }

        Cell newCell = map.getCell(x, y);
        newCell.setActor(player);
    }

    public static GameLogic getLogic() {
        return logic;
    }

    public GameLogic(GameMap map) {
        logic = this;
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
        if (isGameOver()) {
            return;
        }

        Skeleton monster = getClosestMonster();
        if (monster != null) {
            monster.takeTurn(map.getPlayer());
        }
    }

    public boolean isGameOver() {
        return !map.getPlayer().isAlive();
    }

    public GameMap getMap() {
        return map;
    }

    public void saveGame() {
        Player player = map.getPlayer();
        GameStateHandler handler = new GameStateHandler();
        handler.saveToDB(map.toMapString(), player.getX(), player.getY(), player.getHealth(), getPlayerInventory());
    }

    public void loadGame() {
        GameStateHandler handler = new GameStateHandler();
        GameState state = handler.loadFromDB();

        if (state != null) {
            this.map = MapLoader.loadMap(state.map);
            map.getPlayer().setHealth(state.playerHealth);
            for (String itemName : state.inventory) {
                map.getPlayer().loadItemToInventory(itemName);
            }
        }
    }

    public List<Item> getPlayerInventory() {    // ez legyen BENT
        return map.getPlayer().getInventory();
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

    private Skeleton getClosestMonster() {
        Skeleton closestMonster = null;
        int closestDistance = Integer.MAX_VALUE;
        Player player = map.getPlayer();

        for (Skeleton monster : getMonsters()) {
            int distance = Math.abs(player.getX() - monster.getX()) + Math.abs(player.getY() - monster.getY());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestMonster = monster;
            }
        }

        return closestMonster;
    }
}
