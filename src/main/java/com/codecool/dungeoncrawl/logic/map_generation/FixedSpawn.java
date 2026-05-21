package com.codecool.dungeoncrawl.logic.map_generation;

import com.codecool.dungeoncrawl.data.cells.CellType;

public record FixedSpawn(
        int x,
        int y,
        CellType type
) {
}