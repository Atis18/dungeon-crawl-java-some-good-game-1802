package com.codecool.dungeoncrawl.data.gamestate;

import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameStateHandler {
    Connection conn = null;

    public void saveToDB(String map, int player_x, int player_y, int player_health, List<Item> playerInventory){
        try {
            conn = DatabaseManager.getConnection();

            PreparedStatement gameState = conn.prepareStatement("insert into game_state(id, map, player_x, player_y, player_health) values (?,?,?,?,?)" +
                    "on conflict (id) do update set " +
                    "map = EXCLUDED.map,\n" +
                    "player_x = EXCLUDED.player_x,\n" +
                    "player_y = EXCLUDED.player_y,\n" +
                    "player_health = EXCLUDED.player_health");
            {
                gameState.setInt(1, 1);
                gameState.setString(2, map);
                gameState.setInt(3, player_x);
                gameState.setInt(4, player_y);
                gameState.setInt(5, player_health);
                gameState.addBatch();
            }
            gameState.executeBatch();
            gameState.close();

            PreparedStatement delete = conn.prepareStatement(
                    "DELETE FROM game_inventory"
            );
            delete.executeUpdate();
            delete.close();

            PreparedStatement inventory = conn.prepareStatement(
                    "INSERT INTO game_inventory (item_name) VALUES (?)"
            );
            for (Item item : playerInventory) {
                inventory.setString(1, item.getName());
                inventory.addBatch();
            }
            inventory.executeBatch();
            inventory.close();

            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public GameState loadFromDB(){
        try {
            conn = DatabaseManager.getConnection();
            PreparedStatement gameState = conn.prepareStatement("select * from game_state");

            ResultSet gsResult = gameState.executeQuery();

            if (gsResult.next()){
                String map = gsResult.getString("map");
                int playerX = gsResult.getInt("player_x");
                int playerY = gsResult.getInt("player_y");
                int playerHealth = gsResult.getInt("player_health");
            gameState.close();
            gsResult.close();

            PreparedStatement inventory = conn.prepareStatement("select * from game_inventory");
            List<String> inventoryItems = new ArrayList<>();
            ResultSet invResult = inventory.executeQuery();
            while (invResult.next()) {
                inventoryItems.add(invResult.getString("item_name"));
            }


            conn.close();
            return new GameState(map, playerX, playerY, playerHealth, inventoryItems);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
