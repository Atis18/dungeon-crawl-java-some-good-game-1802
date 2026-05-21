package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.actors.Actor;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.gamestate.GameState;
import com.codecool.dungeoncrawl.data.gamestate.GameStateHandler;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private static GameLogic logic;
    private GameMap map;

    public GameLogic() {
        logic = this;
        this.map = MapLoader.loadMap();
    }

    public static GameLogic getLogic(){
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
            monster.act(map.getPlayer());
        }
    }

    public boolean isGameOver() {
        return !map.getPlayer().isAlive();
    }

    public GameMap getMap() {
        return map;
    }

    public void restartGame() {
        String playerName = getPlayerName();
        this.map = MapLoader.loadMap();
        setPlayerName(playerName);
    }

    public void saveGame(){
        Player player = map.getPlayer();
        GameStateHandler handler = new GameStateHandler();
        handler.saveToDB(map.toMapString(), player.getName(), player.getX(), player.getY(), player.getHealth(), getPlayerInventory());
    }

    public void loadGame(){
        GameStateHandler handler = new GameStateHandler();
        GameState state = handler.loadFromDB();

        if (state != null){
            this.map = MapLoader.loadMap(state.map);
            map.getPlayer().setHealth(state.playerHealth);
            map.getPlayer().setName(state.player_name);
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
