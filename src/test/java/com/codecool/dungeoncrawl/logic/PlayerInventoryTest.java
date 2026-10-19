package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.items.HealPotion;
import com.codecool.dungeoncrawl.data.items.Key;
import com.codecool.dungeoncrawl.data.items.Sword;
import com.codecool.dungeoncrawl.data.items.Armor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInventoryTest {

    @Test
    void healingRestoresTwentyHealthAndConsumesPotion() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        player.setHealth(50);
        player.getInventory().add(new HealPotion());

        player.heal();

        assertEquals(70, player.getHealth());
        assertTrue(player.getInventory().isEmpty());
    }

    @Test
    void healingDoesNotExceedMaximumHealth() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        player.setHealth(95);
        player.getInventory().add(new HealPotion());

        player.heal();

        assertEquals(100, player.getHealth());
        assertTrue(player.getInventory().isEmpty());
    }

    @Test
    void healingWithoutPotionDoesNothing() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        player.setHealth(50);

        player.heal();

        assertEquals(50, player.getHealth());
    }

    @Test
    void collectingSwordEquipsItAndIncreasesAttack() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        Cell itemCell = map.getCell(2, 1);
        CellType.SWORD.apply(itemCell, map);
        Skeleton enemy = new Skeleton(map.getCell(3, 1), MonsterTypes.FIGHTER);

        player.move(1, 0);
        player.attack(enemy);

        assertEquals(2, player.getX());
        assertNotNull(player.getEquippedSword());
        // Sword increases the base 5 attack to 6; fighter armor absorbs 2.
        assertEquals(8, enemy.getHealth());
        assertNull(itemCell.getItem());
    }

    @Test
    void collectingArmorEquipsItAndReducesDamage() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        Cell itemCell = map.getCell(2, 1);
        CellType.ARMOR.apply(itemCell, map);

        player.move(1, 0);
        player.takeDamage(5);

        assertNotNull(player.getEquippedArmor());
        assertEquals("player_armored", player.getTileName());
        assertEquals(96, player.getHealth());
        assertNull(itemCell.getItem());
    }

    @Test
    void collectingPotionAddsItToInventory() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        Cell itemCell = map.getCell(2, 1);
        CellType.HEALPOTION.apply(itemCell, map);

        player.move(1, 0);

        assertEquals(1, player.getInventory().size());
        assertTrue(player.getInventory().get(0) instanceof HealPotion);
        assertNull(itemCell.getItem());
        assertEquals(CellType.FLOOR, itemCell.getType());
    }

    @Test
    void closedDoorCannotBeEnteredWithoutKey() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        map.getCell(2, 1).setType(CellType.DOOR);

        player.move(1, 0);

        assertEquals(1, player.getX());
        assertFalse(player.steppedOnDoorWithKey());
    }

    @Test
    void keyUnlocksDoorAndIsConsumed() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        map.getCell(2, 1).setType(CellType.DOOR);
        player.getInventory().add(new Key());

        player.move(1, 0);

        assertEquals(2, player.getX());
        assertTrue(player.steppedOnDoorWithKey());
        assertTrue(player.getInventory().isEmpty());
    }

    @Test
    void doorFlagCanBeReset() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));
        map.getCell(2, 1).setType(CellType.DOOR);
        player.getInventory().add(new Key());
        player.move(1, 0);

        player.resetDoorFlag();

        assertFalse(player.steppedOnDoorWithKey());
    }

    @Test
    void savedInventoryItemNamesAreRestored() {
        GameMap map = new GameMap(4, 3, CellType.FLOOR);
        Player player = new Player(map.getCell(1, 1));

        player.loadItemToInventory("key");
        player.loadItemToInventory("healpotion");
        player.loadItemToInventory("sword");

        assertEquals(3, player.getInventory().size());
        assertTrue(player.getInventory().stream().anyMatch(item -> item instanceof Key));
        assertTrue(player.getInventory().stream().anyMatch(item -> item instanceof HealPotion));
        assertTrue(player.getInventory().stream().anyMatch(item -> item instanceof Sword));
    }
}
