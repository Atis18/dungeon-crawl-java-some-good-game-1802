package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.CellType;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.items.Key;
import com.codecool.dungeoncrawl.data.items.Sword;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {

    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map.txt");
        return loadFromScanner(new Scanner(is));
    }

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
        switch (symbol) {

            case ' ':
                cell.setType(CellType.EMPTY);
                break;

            case '#':
                cell.setType(CellType.WALL);
                break;

            case '.':
                cell.setType(CellType.FLOOR);
                break;

            case 's':
                cell.setType(CellType.FLOOR);
                new Skeleton(cell);
                break;

            case 'f':
                cell.setType(CellType.SAVE);
                break;

            case 'l':
                cell.setType(CellType.LOAD);
                break;

            case '@':
                cell.setType(CellType.FLOOR);
                map.setPlayer(new Player(cell));
                break;

            case 'k':
                cell.setType(CellType.KEY);
                cell.setItem(new Key());
                break;

            case 'w':
                cell.setType(CellType.SWORD);
                cell.setItem(new Sword());
                break;

            default:
                throw new RuntimeException(
                        "Unrecognized character: '" + symbol + "'"
                );
        }
    }
}