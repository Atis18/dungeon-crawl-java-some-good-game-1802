package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.CellType;
import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.MonsterTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {
    GameMap gameMap = new GameMap(3, 3, CellType.FLOOR);

    @Test
    void moveUpdatesCells() {
        Player player = new Player(gameMap.getCell(1, 1));
        player.move(1, 0);

        assertEquals(2, player.getX());
        assertEquals(1, player.getY());
        assertEquals(null, gameMap.getCell(1, 1).getActor());
        assertEquals(player, gameMap.getCell(2, 1).getActor());
    }

    @Test
    void playerHasName() {
        Player player = new Player(gameMap.getCell(1, 1), "  Ada  ");

        assertEquals("Ada", player.getName());
    }

    @Test
    void playerUsesDefaultNameWhenNameIsBlank() {
        Player player = new Player(gameMap.getCell(1, 1), " ");

        assertEquals(Player.getDefaultName(), player.getName());
    }

    @Test
    void cannotMoveIntoWall() {
        gameMap.getCell(2, 1).setType(CellType.WALL);
        Player player = new Player(gameMap.getCell(1, 1));
        player.move(1, 0);

        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    void cannotMoveOutOfMap() {
        Player player = new Player(gameMap.getCell(2, 1));
        player.move(1, 0);

        assertEquals(2, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    void cannotMoveIntoAnotherActor() {
        Player player = new Player(gameMap.getCell(1, 1));
        Skeleton skeleton = new Skeleton(gameMap.getCell(2, 1));
        player.move(1, 0);

        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
        assertEquals(2, skeleton.getX());
        assertEquals(1, skeleton.getY());
        assertEquals(skeleton, gameMap.getCell(2, 1).getActor());
    }

    @Test
    void assassinMovesTwiceTowardPlayer() {
        GameMap largerMap = new GameMap(6, 1, CellType.FLOOR);
        Player player = new Player(largerMap.getCell(0, 0));
        Skeleton skeleton = new Skeleton(largerMap.getCell(5, 0), MonsterTypes.ASSASSIN);

        skeleton.takeTurn(player);

        assertEquals(3, skeleton.getX());
    }

    @Test
    void mageAttacksFromRange() {
        GameMap largerMap = new GameMap(5, 1, CellType.FLOOR);
        Player player = new Player(largerMap.getCell(0, 0));
        Skeleton skeleton = new Skeleton(largerMap.getCell(3, 0), MonsterTypes.MAGE);

        skeleton.takeTurn(player);

        assertEquals(100 - MonsterTypes.MAGE.getAttackPower(), player.getHealth());
        assertEquals(3, skeleton.getX());
    }
}
