package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.Item;

import java.util.List;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.gamestate.GameState;
import com.codecool.dungeoncrawl.data.gamestate.GameStateHandler;

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


    public GameMap getMap() {
        return map;
    }

    public void saveGame(){
        Player player = map.getPlayer();
        GameStateHandler handler = new GameStateHandler();
        handler.saveToDB(map.toMapString(), player.getX(), player.getY(), player.getHealth(), getPlayerInventory());
    }

    public void loadGame(){
        GameStateHandler handler = new GameStateHandler();
        GameState state = handler.loadFromDB();

        if (state != null){
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

}
