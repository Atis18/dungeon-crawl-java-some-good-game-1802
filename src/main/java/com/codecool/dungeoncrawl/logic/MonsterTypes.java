package com.codecool.dungeoncrawl.logic;

public enum MonsterTypes {
    ASSASSIN(6, 6, 1, 2),
    FIGHTER(12, 3, 1, 1),
    MAGE(5, 4, 3, 1);

    private final int health;
    private final int attackPower;
    private final int attackRange;
    private final int movementSpeed;

    MonsterTypes(int health, int attackPower, int attackRange, int movementSpeed) {
        this.health = health;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.movementSpeed = movementSpeed;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }
}
