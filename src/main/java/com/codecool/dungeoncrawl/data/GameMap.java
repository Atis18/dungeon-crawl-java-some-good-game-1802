package com.codecool.dungeoncrawl.data;

import com.codecool.dungeoncrawl.data.actors.Player;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String toMapString() {
        StringBuilder sb = new StringBuilder();

        sb.append(width).append(" ").append(height).append("\n");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[x][y];

                if (cell.getActor() != null) {
                    sb.append(actorToChar(cell.getActor().getTileName()));
                } else {
                    sb.append(cellTypeToChar(cell.getType()));
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private char cellTypeToChar(CellType type) {
        switch (type) {
            case EMPTY:
                return ' ';
            case FLOOR:
                return '.';
            case WALL:
                return '#';
            case SAVE:
                return 'f';
            case LOAD:
                return 'l';
            case SWORD:
                return 'w';
            case KEY:
                return 'k';
            default:
                throw new RuntimeException("Unknown cell type: " + type);
        }
    }

    private char actorToChar(String tileName) {
        switch (tileName) {
            case "player":
                return '@';
            case "skeleton":
                return 's';
            default:
                throw new RuntimeException("Unknown actor: " + tileName);
        }
    }
}
