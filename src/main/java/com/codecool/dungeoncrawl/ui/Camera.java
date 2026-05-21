package com.codecool.dungeoncrawl.ui;

import com.codecool.dungeoncrawl.data.GameMap;

public class Camera {

    private int x;
    private int y;

    private int viewWidth;
    private int viewHeight;

    private GameMap map;

    public Camera(int viewWidth, int viewHeight, GameMap map) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.map = map;
    }

    public void setMap(GameMap map) {
        this.map = map;
        clamp();
    }

    public void follow(int targetX, int targetY) {
        x = targetX - viewWidth / 2;
        y = targetY - viewHeight / 2;

        clamp();
    }

    private void clamp() {
        x = Math.max(0, Math.min(x, map.getWidth() - viewWidth));
        y = Math.max(0, Math.min(y, map.getHeight() - viewHeight));
    }

    public int getX() { return x; }
    public int getY() { return y; }
}