package com.codecool.dungeoncrawl.data.cells;

import com.codecool.dungeoncrawl.data.GameMap;

@FunctionalInterface
public interface CellInitializer {
    void initialize(Cell cell, GameMap map);
}
