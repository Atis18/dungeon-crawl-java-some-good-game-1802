package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.items.Key;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    private GameMap map;
    private Player player;
    private GameLogic logic;

    @BeforeEach
    void setUp() {
        map = new GameMap(8, 8, CellType.FLOOR);
        player = new Player(map.getCell(2, 2), "Hero");
        map.setPlayer(player);
        logic = new GameLogic(map);
    }

    @Test
    void providesCurrentMap() {
        assertSame(map, logic.getMap());
    }

    @Test
    void reportsMapDimensions() {
        assertEquals(8.0, logic.getMapWidth());
        assertEquals(8.0, logic.getMapHeight());
    }

    @Test
    void looksUpCellsOnCurrentMap() {
        assertSame(map.getCell(1, 4), logic.getCell(1, 4));
    }

    @Test
    void exposesCurrentLogicInstance() {
        assertSame(logic, GameLogic.getLogic());
    }

    @Test
    void updatesPlayerName() {
        logic.setPlayerName("Explorer");
        assertEquals("Explorer", logic.getPlayerName());
        assertEquals("Explorer", player.getName());
    }

    @Test
    void reportsPlayerHealthAsText() {
        player.takeDamage(10);
        assertEquals("90", logic.getPlayerHealth());
    }

    @Test
    void exposesPlayersInventory() {
        player.getInventory().add(new Key());
        assertSame(player.getInventory(), logic.getPlayerInventory());
        assertEquals(1, logic.getPlayerInventory().size());
    }

    @Test
    void fightDamagesAdjacentEnemy() {
        Skeleton enemy = new Skeleton(map.getCell(3, 2), MonsterTypes.ASSASSIN);
        int before = enemy.getHealth();

        logic.fight(player, enemy);

        assertEquals(before - 5, enemy.getHealth());
    }

    @Test
    void fightIgnoresMissingAttacker() {
        Skeleton enemy = new Skeleton(map.getCell(3, 2), MonsterTypes.ASSASSIN);
        int before = enemy.getHealth();

        assertDoesNotThrow(() -> logic.fight(null, enemy));
        assertEquals(before, enemy.getHealth());
    }

    @Test
    void fightIgnoresMissingDefender() {
        assertDoesNotThrow(() -> logic.fight(player, null));
    }

    @Test
    void fightCannotReachDistantEnemy() {
        Skeleton enemy = new Skeleton(map.getCell(6, 6), MonsterTypes.ASSASSIN);
        int before = enemy.getHealth();

        logic.fight(player, enemy);

        assertEquals(before, enemy.getHealth());
    }

    @Test
    void defeatedPlayerCannotAttack() {
        Skeleton enemy = new Skeleton(map.getCell(3, 2), MonsterTypes.ASSASSIN);
        player.takeDamage(1000);
        int before = enemy.getHealth();

        logic.fight(player, enemy);

        assertEquals(before, enemy.getHealth());
    }

    @Test
    void runningGameIsNotOver() {
        assertFalse(logic.isGameOver());
    }

    @Test
    void gameEndsWhenPlayerDies() {
        player.takeDamage(1000);
        assertTrue(logic.isGameOver());
    }

    @Test
    void monstersTurnWithoutMonstersDoesNotHurtPlayer() {
        logic.makeMonstersAct();
        assertEquals(100, player.getHealth());
    }

    @Test
    void adjacentFighterAttacksDuringMonstersTurn() {
        new Skeleton(map.getCell(3, 2), MonsterTypes.FIGHTER);

        logic.makeMonstersAct();

        assertEquals(97, player.getHealth());
    }

    @Test
    void onlyClosestMonsterActsDuringATurn() {
        Skeleton adjacent = new Skeleton(map.getCell(3, 2), MonsterTypes.FIGHTER);
        Skeleton distant = new Skeleton(map.getCell(6, 6), MonsterTypes.FIGHTER);

        logic.makeMonstersAct();

        assertEquals(97, player.getHealth());
        assertSame(adjacent, map.getCell(3, 2).getActor());
        assertSame(distant, map.getCell(6, 6).getActor());
    }

    @Test
    void monstersDoNotActAfterGameOver() {
        Skeleton enemy = new Skeleton(map.getCell(4, 2), MonsterTypes.FIGHTER);
        player.takeDamage(1000);

        logic.makeMonstersAct();

        assertSame(enemy, map.getCell(4, 2).getActor());
        assertNull(map.getCell(3, 2).getActor());
    }

    @Test
    void restartPreservesPlayerNameAndCreatesNewMap() {
        GameMap original = logic.getMap();
        logic.setPlayerName("Explorer");
        player.takeDamage(1000);

        logic.restartGame();

        assertNotSame(original, logic.getMap());
        assertEquals("Explorer", logic.getPlayerName());
        assertFalse(logic.isGameOver());
    }
}
