package com.codecool.dungeoncrawl.logic.map_generation;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.CellType;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    /**
     MapGenerator generator = new MapGenerator(30, 20);

     // guaranteed player spawn
     generator.addFixedSpawn(
     new FixedSpawn(2, 2, '@')
     );

     // guaranteed sword
     generator.addFixedSpawn(
     new FixedSpawn(10, 10, 'w')
     );

     // one skeleton somewhere in rectangle
     generator.addRegionSpawn(
     new RegionSpawn(
     5, 10,
     0, 5,
     's',
     1
     )
     );

     // three keys in another region
     generator.addRegionSpawn(
     new RegionSpawn(
     15, 25,
     5, 15,
     'k',
     3
     )
     );


     GameMap map = generator.generate(); */

    private final Random random = new Random();

    private final int width;
    private final int height;

    private final List<FixedSpawn> fixedSpawns = new ArrayList<>();
    private final List<RegionSpawn> regionSpawns = new ArrayList<>();

    public MapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addFixedSpawn(FixedSpawn spawn) {
        fixedSpawns.add(spawn);
    }

    public void addRegionSpawn(RegionSpawn spawn) {
        regionSpawns.add(spawn);
    }

    public GameMap generate() {

        GameMap map = new GameMap(width, height, CellType.FLOOR);

        // Example: surround with walls
        createBorderWalls(map);

        // Guaranteed placements
        for (FixedSpawn spawn : fixedSpawns) {
            place(spawn.symbol,
                    map.getCell(spawn.x, spawn.y),
                    map);
        }

        // Random region placements
        for (RegionSpawn spawn : regionSpawns) {

            int placed = 0;

            while (placed < spawn.amount) {

                int x = random.nextInt(
                        spawn.maxX - spawn.minX + 1
                ) + spawn.minX;

                int y = random.nextInt(
                        spawn.maxY - spawn.minY + 1
                ) + spawn.minY;

                Cell cell = map.getCell(x, y);

                // Only place on empty floor
                if (cell.getActor() == null &&
                        cell.getItem() == null &&
                        cell.getType() == CellType.FLOOR) {

                    place(spawn.symbol, cell, map);
                    placed++;
                }
            }
        }

        return map;
    }

    private void createBorderWalls(GameMap map) {

        for (int x = 0; x < width; x++) {
            map.getCell(x, 0).setType(CellType.WALL);
            map.getCell(x, height - 1).setType(CellType.WALL);
        }

        for (int y = 0; y < height; y++) {
            map.getCell(0, y).setType(CellType.WALL);
            map.getCell(width - 1, y).setType(CellType.WALL);
        }
    }

    private void place(char symbol, Cell cell, GameMap map) {
        MapLoader.processCell(symbol, cell, map);
    }
}
