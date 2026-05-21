package com.codecool.dungeoncrawl.data.actors;

import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.data.cells.CellType;
import com.codecool.dungeoncrawl.data.Drawable;
import com.codecool.dungeoncrawl.logic.GameLogic;

public abstract class Actor implements Drawable {
    private Cell cell;
    private int health = 10;
    private int attackPower = 1;
    private int attackRange = 1;
    private int armor = 0;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getType() != CellType.WALL && nextCell.getActor() == null) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
            if(nextCell.getType() == CellType.SAVE){
                GameLogic.getLogic().saveGame();
            }
            if(nextCell.getType() == CellType.LOAD){
                GameLogic.getLogic().loadGame();
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void attack(Actor target) {
        if (target == null || !isInAttackRange(target) || !isAlive()) {
            return;
        }

        target.takeDamage(attackPower);
    }

    public void takeDamage(int damage) {
        if (damage <= 0 || !isAlive()) {
            return;
        }

        int actualDamage = Math.max(0, damage - armor);
        health = Math.max(0, health - actualDamage);
        if (health == 0) {
            die();
        }
    }
    public void setArmor(int armor) {
        this.armor = armor;
    }

    public boolean isInAttackRange(Actor target) {
        if (target == null) {
            return false;
        }

        int distance = Math.abs(getX() - target.getX()) + Math.abs(getY() - target.getY());
        return distance <= attackRange;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void setHealth(int health) {
        this.health = Math.max(1, health);
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = Math.max(1, attackPower);
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = Math.max(1, attackRange);
    }

    public void die() {
        cell.setActor(null);
    }
}
