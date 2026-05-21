package com.codecool.dungeoncrawl.logic.map_generation;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.cells.Cell;

import java.util.function.BiConsumer;

public record LootSpawn(
        BiConsumer<Cell, GameMap> spawner,
        int quantity
) {
}