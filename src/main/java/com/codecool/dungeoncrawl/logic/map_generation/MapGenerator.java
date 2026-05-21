package com.codecool.dungeoncrawl.logic.map_generation;

import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    private final Random random = new Random();

    private final int width;
    private final int height;

    private final int roomCount;
    private Room startRoom;

    private final List<LootSpawn> lootSpawns = new ArrayList<>();

    public MapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.roomCount = calculateRoomCount();
    }

    private int calculateRoomCount() {

        int mapArea = width * height;

        int averageRoomArea = 30;

        double targetCoverage = 0.18;

        int estimatedRooms = (int) (
                (mapArea * targetCoverage) / averageRoomArea
        );

        // Clamp to prevent too few or too many rooms
        return Math.max(5, Math.min(estimatedRooms, 50));
    }

    public GameMap generate() {

        GameMap map = new GameMap(width, height, CellType.WALL);

        List<Room> rooms = generateRooms(map);

        this.startRoom = rooms.get(0);

        connectRooms(map, rooms);

        placeSpawns(map, rooms);

        return map;
    }

    private void placeSpawns(GameMap map, List<Room> rooms) {

        placePlayer(map, rooms);

        placeEnemies(map, rooms);

        placeLoot(map, rooms);

        placeDoor(map, rooms);
    }

    private void placeDoor(GameMap map, List<Room> rooms) {

        if (rooms.isEmpty()) {
            return;
        }

        Room lastRoom = rooms.get(rooms.size() - 1);

        spawnRandomInRoom(map, lastRoom, CellType.DOOR);
    }

    private void placePlayer(GameMap map, List<Room> rooms) {

        if (rooms.isEmpty()) {
            return;
        }

        Room startRoom = rooms.get(0);

        int x = startRoom.centerX();
        int y = startRoom.centerY();

        Cell cell = map.getCell(x, y);

        CellType.PLAYER.apply(cell, map);
    }

    private int calculateEnemiesForRoom(Room room) {

        // 33% chance the room has no enemies
        if (random.nextDouble() < 0.33) {
            return 0;
        }

        int roomArea = room.width * room.height;

        // Scale by room size
        int enemies = roomArea / 12;

        // Add slight randomness
        enemies += random.nextInt(2);

        // Clamp between 1 and 4
        return Math.max(1, Math.min(enemies, 4));
    }

    private void placeEnemies(GameMap map, List<Room> rooms) {

        for (int i = 1; i < rooms.size(); i++) {

            Room room = rooms.get(i);

            int enemyCount = calculateEnemiesForRoom(room);

            for (int j = 0; j < enemyCount; j++) {
                spawnRandomInRoom(map, room, CellType.SKELETON);
            }
        }
    }

    private void spawnRandomInRoom(GameMap map, Room room, CellType type) {
        int attempts = 0;
        while (attempts < 100) {
            attempts++;
            int x = random.nextInt(room.width - 2) + room.x + 1;

            int y = random.nextInt(room.height - 2) + room.y + 1;

            Cell cell = map.getCell(x, y);

            if (!isValidSpawnCell(cell)) {
                continue;
            }

            type.apply(cell, map);
            return;
        }
    }

    private void placeLoot(GameMap map, List<Room> rooms) {

        if (rooms.isEmpty()) {
            return;
        }

        for (LootSpawn loot : lootSpawns) {

            for (int i = 0; i < loot.quantity(); i++) {

                // Pick a random non-starter room
                Room room = rooms.get(
                        random.nextInt(rooms.size() - 1) + 1
                );

                spawnRandomInRoom(map, room, loot.type());
            }
        }
    }

    public void addLootSpawn(CellType type, int quantity) {
        lootSpawns.add(new LootSpawn(type, quantity));
    }

    private List<Room> generateRooms(GameMap map) {

        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < roomCount; i++) {

            int roomWidth = random.nextInt(4) + 4;
            int roomHeight = random.nextInt(4) + 4;

            int x = random.nextInt(width - roomWidth - 2) + 1;
            int y = random.nextInt(height - roomHeight - 2) + 1;

            Room newRoom = new Room(x, y, roomWidth, roomHeight);

            boolean overlaps = false;

            for (Room otherRoom : rooms) {

                if (newRoom.intersects(otherRoom)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {

                carveRoom(map, newRoom);

                rooms.add(newRoom);
            }
        }

        return rooms;
    }


    private void connectRooms(
            GameMap map,
            List<Room> rooms
    ) {

        for (int i = 1; i < rooms.size(); i++) {

            Room previous = rooms.get(i - 1);
            Room current = rooms.get(i);

            int x1 = previous.centerX();
            int y1 = previous.centerY();

            int x2 = current.centerX();
            int y2 = current.centerY();

            carveHorizontalTunnel(map, x1, x2, y1);

            carveVerticalTunnel(map, y1, y2, x2);
        }
    }

    private void carveRoom(GameMap map, Room room) {

        for (int y = room.y;
             y < room.y + room.height;
             y++) {

            for (int x = room.x;
                 x < room.x + room.width;
                 x++) {

                map.getCell(x, y)
                        .setType(CellType.FLOOR);
            }
        }
    }

    private void carveHorizontalTunnel(GameMap map, int x1, int x2, int y) {

        for (int x = Math.min(x1, x2);
             x <= Math.max(x1, x2);
             x++) {

            map.getCell(x, y)
                    .setType(CellType.FLOOR);
        }
    }

    private void carveVerticalTunnel(GameMap map, int y1, int y2, int x) {

        for (int y = Math.min(y1, y2);
             y <= Math.max(y1, y2);
             y++) {

            map.getCell(x, y)
                    .setType(CellType.FLOOR);
        }
    }

    private boolean isValidSpawnCell(Cell cell) {

        return cell.getType() == CellType.FLOOR &&
                cell.getActor() == null &&
                cell.getItem() == null;
    }

    public Room getStartRoom() {
        return startRoom;
    }
}