package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.items.Bomb;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BombTest {

    @Test
    void bombDamagesEnemiesWithinRangeButNotOutside() {
        GameMap map = new GameMap(11, 11, CellType.FLOOR);
        Player player = new Player(map.getCell(5, 5));
        Skeleton closeEnemy = new Skeleton(map.getCell(7, 5), MonsterTypes.FIGHTER);
        Skeleton farEnemy = new Skeleton(map.getCell(9, 5), MonsterTypes.FIGHTER);

        new Bomb().use(player);

        assertFalse(closeEnemy.isAlive());
        assertEquals(MonsterTypes.FIGHTER.getHealth(), farEnemy.getHealth());
    }

    @Test
    void bombNeverDamagesItsUser() {
        GameMap map = new GameMap(11, 11, CellType.FLOOR);
        Player player = new Player(map.getCell(5, 5));
        new Skeleton(map.getCell(6, 5), MonsterTypes.MAGE);

        new Bomb().use(player);

        assertEquals(100, player.getHealth());
        assertSame(player, map.getCell(5, 5).getActor());
    }

    @Test
    void bombCanBeUsedAtMapEdgeWithoutLeavingMap() {
        GameMap map = new GameMap(5, 5, CellType.FLOOR);
        Player player = new Player(map.getCell(0, 0));
        Skeleton enemy = new Skeleton(map.getCell(1, 1), MonsterTypes.FIGHTER);

        new Bomb().use(player);

        assertFalse(enemy.isAlive());
        assertEquals(100, player.getHealth());
    }
}
