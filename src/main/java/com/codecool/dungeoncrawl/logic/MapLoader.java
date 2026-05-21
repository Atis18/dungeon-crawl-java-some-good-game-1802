package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.GameMap;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String map) {
        return loadFromScanner(new Scanner(map));
    }

    private static GameMap loadFromScanner(Scanner scanner) {
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap gameMap = new GameMap(width, height, CellType.EMPTY);

        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();

            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = gameMap.getCell(x, y);
                    processCell(line.charAt(x), cell, gameMap);
                }
            }
        }

        scanner.close();
        return gameMap;
    }

    public static void processCell(char symbol, Cell cell, GameMap map) {
        if (Skeleton.isMonsterSymbol(symbol)) {
            cell.setType(CellType.FLOOR);
            Skeleton.spawn(cell, symbol);
            return;
        }

        CellType.fromSymbol(symbol).apply(cell, map);
    }
}