package com.codecool.dungeoncrawl.data.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DATABASE_URL = dotenv.get("DB_URL");
    private static final String DATABASE_USER = dotenv.get("DB_USER");
    private static final String DATABASE_PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE_URL,
                DATABASE_USER,
                DATABASE_PASSWORD
        );
    }

    public static void initializeDatabase() {
        String createGameStateTable =
                "CREATE TABLE IF NOT EXISTS game_state (" +
                        "id INTEGER PRIMARY KEY, " +
                        "map TEXT NOT NULL, " +
                        "player_name TEXT NOT NULL, " +
                        "player_x INTEGER NOT NULL, " +
                        "player_y INTEGER NOT NULL, " +
                        "player_health INTEGER NOT NULL" +
                        ");";
        String createInventoryTable =
                "CREATE TABLE IF NOT EXISTS game_inventory (" +
                        "id SERIAL PRIMARY KEY, " +
                        "item_name TEXT NOT NULL" +
                        ");";
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(createGameStateTable);
            statement.execute(createInventoryTable);
            System.out.println("Database initialized");
        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize database", e);
        }
    }
}
