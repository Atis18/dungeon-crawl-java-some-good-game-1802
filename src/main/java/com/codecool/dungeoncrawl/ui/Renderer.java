package com.codecool.dungeoncrawl.ui;

import com.codecool.dungeoncrawl.data.cells.Cell;
import com.codecool.dungeoncrawl.logic.GameLogic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {

    private final GraphicsContext gc;
    private final GameLogic logic;
    private final Camera camera;

    public Renderer(GraphicsContext gc, GameLogic logic, Camera camera) {
        this.gc = gc;
        this.logic = logic;
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public void render() {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        int tileSize = Tiles.TILE_WIDTH;

        int startX = camera.getX();
        int startY = camera.getY();

        int width = (int)(gc.getCanvas().getWidth() / tileSize);
        int height = (int)(gc.getCanvas().getHeight() / tileSize);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int worldX = startX + x;
                int worldY = startY + y;

                if (worldX >= logic.getMapWidth() || worldY >= logic.getMapHeight())
                    continue;

                Cell cell = logic.getCell(worldX, worldY);

                if (cell.getActor() != null) {
                    Tiles.drawTile(gc, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(gc, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(gc, cell, x, y);
                }
            }
        }
    }
}