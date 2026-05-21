package com.codecool.dungeoncrawl.logic.map_generation;

import com.codecool.dungeoncrawl.data.cells.CellType;

public record RegionSpawn(
        int minX,
        int maxX,
        int minY,
        int maxY,
        CellType type,
        int amount
) {
}