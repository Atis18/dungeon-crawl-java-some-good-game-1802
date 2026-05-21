package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.logic.MonsterTypes;

public class Skeleton extends Actor {
    private static final char ASSASSIN_MAP_SYMBOL = 'i';
    private static final char FIGHTER_MAP_SYMBOL = 's';
    private static final char MAGE_MAP_SYMBOL = 'm';

    private final MonsterTypes monsterType;

    public Skeleton(Cell cell) {
        this(cell, MonsterTypes.FIGHTER);
    }

    public Skeleton(Cell cell, MonsterTypes monsterType) {
        super(cell);
        this.monsterType = monsterType;
        setHealth(monsterType.getHealth());
        setAttackPower(monsterType.getAttackPower());
        setAttackRange(monsterType.getAttackRange());

        if (monsterType == MonsterTypes.FIGHTER) {
            setArmor(2);
        }
    }

    public MonsterTypes getMonsterType() {
        return monsterType;
    }

    public static boolean isMonsterSymbol(char mapSymbol) {
        return mapSymbol == ASSASSIN_MAP_SYMBOL || mapSymbol == FIGHTER_MAP_SYMBOL || mapSymbol == MAGE_MAP_SYMBOL;
    }

    public static MonsterTypes getMonsterTypeByMapSymbol(char mapSymbol) {
        switch (mapSymbol) {
            case ASSASSIN_MAP_SYMBOL:
                return MonsterTypes.ASSASSIN;
            case MAGE_MAP_SYMBOL:
                return MonsterTypes.MAGE;
            case FIGHTER_MAP_SYMBOL:
                return MonsterTypes.FIGHTER;
            default:
                throw new IllegalArgumentException("Unknown monster symbol: '" + mapSymbol + "'");
        }
    }

    public static Skeleton spawn(Cell cell, char mapSymbol) {
        return new Skeleton(cell, getMonsterTypeByMapSymbol(mapSymbol));
    }

    public void act(Player player) {
        if (player == null || !isAlive()) {
            return;
        }

        switch (getMonsterType()) {
            case ASSASSIN:
                actAsAssassin(player);
                break;
            case MAGE:
                actAsMage(player);
                break;
            case FIGHTER:
            default:
                actAsFighter(player);
                break;
        }
    }

    public void takeTurn(Player player) {
        act(player);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }

    private void actAsAssassin(Player player) {
        for (int step = 0; step < getMonsterType().getMovementSpeed(); step++) {
            if (isInAttackRange(player)) {
                attack(player);
                return;
            }

            if (!moveTowards(player)) {
                return;
            }
        }

        attack(player);
    }

    private void actAsFighter(Player player) {
        if (isInAttackRange(player)) {
            attack(player);
            return;
        }

        moveTowards(player);
    }

    private void actAsMage(Player player) {
        int distance = getDistanceFrom(player);

        if (distance <= 1 && moveAwayFrom(player)) {
            return;
        }

        if (isInAttackRange(player)) {
            attack(player);
            return;
        }

        moveTowards(player);
    }

    private boolean moveTowards(Player player) {
        int dx = Integer.compare(player.getX(), getX());
        int dy = Integer.compare(player.getY(), getY());
        return tryMoveByPriority(dx, dy);
    }

    private boolean moveAwayFrom(Player player) {
        int dx = Integer.compare(getX(), player.getX());
        int dy = Integer.compare(getY(), player.getY());
        return tryMoveByPriority(dx, dy);
    }

    private boolean tryMoveByPriority(int dx, int dy) {
        if (Math.abs(dx) >= Math.abs(dy)) {
            return tryMove(dx, 0) || tryMove(0, dy);
        }

        return tryMove(0, dy) || tryMove(dx, 0);
    }

    private boolean tryMove(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return false;
        }

        try {
            int oldX = getX();
            int oldY = getY();
            move(dx, dy);
            return oldX != getX() || oldY != getY();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private int getDistanceFrom(Player player) {
        return Math.abs(player.getX() - getX()) + Math.abs(player.getY() - getY());
    }
}
