package com.codecool.dungeoncrawl.logic.map_generation;

import com.codecool.dungeoncrawl.data.cells.CellType;

public record LootSpawn(
        CellType type,
        int quantity
) {
}