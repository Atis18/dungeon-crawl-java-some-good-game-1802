package com.codecool.dungeoncrawl.data.gamestate;

import com.codecool.dungeoncrawl.data.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameStateHandler {
    Connection conn = null;

    public void saveToDB(String map, int player_x, int player_y, int player_healt){
        try {
            conn = DatabaseManager.getConnection();

            PreparedStatement ps = conn.prepareStatement("insert into game_state(id, map, player_x, player_y, player_health, inventory) values (?,?,?,?,?,?)" +
                    " on conflict (id) do update set " +
                    "map = EXCLUDED.map,\n" +
                    "            player_x = EXCLUDED.player_x,\n" +
                    "            player_y = EXCLUDED.player_y,\n" +
                    "            player_health = EXCLUDED.player_health,\n" +
                    "            inventory = EXCLUDED.inventory");
            {
                ps.setInt(1, 1);
                ps.setString(2, map);
                ps.setInt(3, player_x);
                ps.setInt(4, player_y);
                ps.setInt(5, player_healt);
                ps.setString(6, "Items");
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public GameState loadFromDB(){
        try {
            conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from game_state");

            ResultSet result = ps.executeQuery();

            if (result.next()){
                String map = result.getString("map");
                int playerX = result.getInt("player_x");
                int playerY = result.getInt("player_y");
                int playerHealth = result.getInt("player_health");
                String inventory = result.getString("inventory");
            ps.close();
            result.close();
            conn.close();
            return new GameState(map, playerX, playerY, playerHealth);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
