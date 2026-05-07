package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.logic.MonsterTypes;

public class Skeleton extends Actor {
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
    }

    public MonsterTypes getMonsterType() {
        return monsterType;
    }

    public void takeTurn(Player player) {
        if (player == null || !isAlive()) {
            return;
        }

        switch (getMonsterType()) {
            case ASSASSIN:
                playAssassinTurn(player);
                break;
            case MAGE:
                playMageTurn(player);
                break;
            case FIGHTER:
            default:
                playFighterTurn(player);
                break;
        }
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }

    private void playAssassinTurn(Player player) {
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

    private void playFighterTurn(Player player) {
        if (isInAttackRange(player)) {
            attack(player);
            return;
        }

        moveTowards(player);
    }

    private void playMageTurn(Player player) {
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
            move(dx, dy);
            return true;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private int getDistanceFrom(Player player) {
        return Math.abs(player.getX() - getX()) + Math.abs(player.getY() - getY());
    }
}
