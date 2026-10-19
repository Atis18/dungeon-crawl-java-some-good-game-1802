package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.items.Armor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActorCombatTest {

    @Test
    void adjacentAttackDealsDamageAfterEnemyArmor() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        Skeleton fighter = new Skeleton(map.getCell(2, 1), MonsterTypes.FIGHTER);

        player.attack(fighter);

        // Fighter armor absorbs 2 points from the player's base attack of 5.
        assertEquals(9, fighter.getHealth());
    }

    @Test
    void attackOutsideRangeDoesNotDealDamage() {
        GameMap map = new GameMap(5, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(0, 1));
        Skeleton enemy = new Skeleton(map.getCell(3, 1), MonsterTypes.FIGHTER);

        player.attack(enemy);

        assertEquals(MonsterTypes.FIGHTER.getHealth(), enemy.getHealth());
    }

    @Test
    void diagonalTileIsNotWithinMeleeRange() {
        GameMap map = new GameMap(3, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(0, 0));
        Skeleton enemy = new Skeleton(map.getCell(1, 1), MonsterTypes.MAGE);

        player.attack(enemy);

        assertEquals(MonsterTypes.MAGE.getHealth(), enemy.getHealth());
    }

    @Test
    void equippedArmorReducesIncomingDamage() {
        GameMap map = new GameMap(3, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        player.equipArmor(new Armor());

        player.takeDamage(7);

        assertEquals(94, player.getHealth());
    }

    @Test
    void armorCanAbsorbAllDamage() {
        GameMap map = new GameMap(3, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        player.equipArmor(new Armor());

        player.takeDamage(1);

        assertEquals(100, player.getHealth());
    }

    @Test
    void nonPositiveDamageHasNoEffect() {
        GameMap map = new GameMap(3, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));

        player.takeDamage(0);
        player.takeDamage(-10);

        assertEquals(100, player.getHealth());
    }

    @Test
    void lethalDamageRemovesActorFromItsCell() {
        GameMap map = new GameMap(3, 3, CellType.FLOOR);
        Cell cell = map.getCell(1, 1);
        Skeleton enemy = new Skeleton(cell, MonsterTypes.MAGE);

        enemy.takeDamage(100);

        assertFalse(enemy.isAlive());
        assertEquals(0, enemy.getHealth());
        assertNull(cell.getActor());
    }

    @Test
    void deadEnemyCannotAttackPlayer() {
        GameMap map = new GameMap(3, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        Skeleton enemy = new Skeleton(map.getCell(2, 1), MonsterTypes.FIGHTER);
        enemy.takeDamage(100);

        enemy.attack(player);

        assertEquals(100, player.getHealth());
    }
}
