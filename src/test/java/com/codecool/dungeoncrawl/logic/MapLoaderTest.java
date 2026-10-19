package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.items.Key;
import com.codecool.dungeoncrawl.data.items.Sword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapLoaderTest {

    @Test
    void mapLoaderCreatesPlayerAndMonsterFromSymbols() {
        GameMap map = MapLoader.loadMap("5 3\n#####\n#@s.#\n#####\n");

        assertEquals(5, map.getWidth());
        assertEquals(3, map.getHeight());
        assertNotNull(map.getPlayer());
        assertEquals(1, map.getPlayer().getX());
        assertEquals(1, map.getPlayer().getY());
        assertTrue(map.getCell(2, 1).getActor() instanceof Skeleton);
    }

    @Test
    void mapLoaderInitializesItemCells() {
        GameMap map = MapLoader.loadMap("6 3\n######\n#@wk.#\n######\n");

        assertTrue(map.getCell(2, 1).getItem() instanceof Sword);
        assertTrue(map.getCell(3, 1).getItem() instanceof Key);
        assertEquals(CellType.WALL, map.getCell(0, 0).getType());
    }

    @Test
    void mapLoaderRejectsUnknownSymbols() {
        assertThrows(RuntimeException.class,
                () -> MapLoader.loadMap("3 1\n@?i\n"));
    }

    @Test
    void mapCanBeSerializedAndLoadedAgainForBasicTiles() {
        GameMap original = MapLoader.loadMap("5 3\n#####\n#@..#\n#####\n");

        GameMap restored = MapLoader.loadMap(original.toMapString());

        assertNotNull(restored.getPlayer());
        assertEquals(1, restored.getPlayer().getX());
        assertEquals(1, restored.getPlayer().getY());
        assertEquals(CellType.WALL, restored.getCell(0, 0).getType());
        assertEquals(CellType.FLOOR, restored.getCell(2, 1).getType());
    }
}
