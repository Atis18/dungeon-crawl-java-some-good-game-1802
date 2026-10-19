package com.codecool.dungeoncrawl.logic.map_generation;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.items.HealPotion;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MapGeneratorTest {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 65;
    private static final long SEED = 12345L;

    private MapGenerator generator(List<LootSpawn> loot) {
        return new MapGenerator(WIDTH, HEIGHT, loot, new Random(SEED));
    }

    @Test
    void createsMapWithRequestedDimensions() {
        GameMap map = generator(List.of()).generate();

        assertEquals(WIDTH, map.getWidth());
        assertEquals(HEIGHT, map.getHeight());
    }

    @Test
    void spawnsPlayerAtCenterOfStartRoom() {
        MapGenerator generator = generator(List.of());
        GameMap map = generator.generate();
        Room startRoom = generator.getStartRoom();
        Player player = map.getPlayer();

        assertNotNull(startRoom);
        assertNotNull(player);
        assertEquals(startRoom.centerX(), player.getX());
        assertEquals(startRoom.centerY(), player.getY());
        assertSame(player, map.getCell(player.getX(), player.getY()).getActor());
    }

    @Test
    void generatedMapContainsAnExitDoor() {
        GameMap map = generator(List.of()).generate();
        int doors = countCells(map, CellType.DOOR);

        assertEquals(1, doors);
    }

    @Test
    void createsWalkableTilesAndKeepsWalls() {
        GameMap map = generator(List.of()).generate();

        assertTrue(countCells(map, CellType.FLOOR) > 0);
        assertTrue(countCells(map, CellType.WALL) > 0);
    }

    @Test
    void monstersSpawnOnlyOnFloorTiles() {
        GameMap map = generator(List.of()).generate();
        int monsters = 0;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() instanceof Skeleton) {
                    monsters++;
                    assertEquals(CellType.FLOOR, cell.getType());
                }
            }
        }

        assertTrue(monsters > 0);
    }

    @Test
    void placesConfiguredLootIntoAccessibleCells() {
        LootSpawn potions = new LootSpawn((cell, map) -> CellType.HEALPOTION.apply(cell, map), 3);
        GameMap map = generator(List.of(potions)).generate();
        int potionCount = 0;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getItem() instanceof HealPotion) {
                    potionCount++;
                    assertEquals(CellType.HEALPOTION, cell.getType());
                    assertNull(cell.getActor());
                }
            }
        }

        assertEquals(3, potionCount);
    }

    @Test
    void allWalkableTilesAreConnectedToPlayerStart() {
        GameMap map = generator(List.of()).generate();
        Player player = map.getPlayer();
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Queue<Cell> queue = new ArrayDeque<>();
        queue.add(player.getCell());
        visited[player.getX()][player.getY()] = true;

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            Cell current = queue.remove();
            for (int[] direction : directions) {
                int x = current.getX() + direction[0];
                int y = current.getY() + direction[1];
                if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT || visited[x][y]) {
                    continue;
                }
                Cell next = map.getCell(x, y);
                if (next.getType() != CellType.WALL) {
                    visited[x][y] = true;
                    queue.add(next);
                }
            }
        }

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (map.getCell(x, y).getType() != CellType.WALL) {
                    assertTrue(visited[x][y]);
                }
            }
        }
    }

    @Test
    void sameRandomSeedProducesSameMap() {
        GameMap first = generator(List.of()).generate();
        GameMap second = generator(List.of()).generate();

        assertEquals(first.toMapString(), second.toMapString());
    }

    private int countCells(GameMap map, CellType type) {
        int count = 0;
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getCell(x, y).getType() == type) {
                    count++;
                }
            }
        }
        return count;
    }
}
