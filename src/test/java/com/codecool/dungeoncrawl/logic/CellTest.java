package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.GameMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    GameMap map = new GameMap(3, 3, CellType.FLOOR);

    @Test
    void getNeighbor() {
        Cell cell = map.getCell(1, 1);
        Cell neighbor = cell.getNeighbor(-1, 0);
        assertEquals(0, neighbor.getX());
        assertEquals(1, neighbor.getY());
    }

    @Test
    void cellOnEdgeReturnsCurrentCell() {
        Cell topCell = map.getCell(1, 0);
        assertSame(topCell, topCell.getNeighbor(0, -1));

        Cell bottomCell = map.getCell(1, 2);
        assertSame(bottomCell, bottomCell.getNeighbor(0, 1));
    }
}