package com.codecool.dungeoncrawl.ui;

import com.codecool.dungeoncrawl.data.Cell;
import com.codecool.dungeoncrawl.data.Chunk;
import com.codecool.dungeoncrawl.logic.GameLogic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {

    private final GraphicsContext gc;
    private final GameLogic logic;

    public Renderer(GraphicsContext gc, GameLogic logic) {
        this.gc = gc;
        this.logic = logic;
    }

    public void render(int startX, int startY) {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 512, 512); // később canvas size

        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int y = 0; y < Chunk.SIZE; y++) {

                int mapX = startX + x;
                int mapY = startY + y;

                if (mapX >= logic.getMapWidth() || mapY >= logic.getMapHeight()) continue;

                Cell cell = logic.getCell(mapX, mapY);

                Tiles.drawTile(
                        gc,
                        cell.getActor() != null ? cell.getActor() : cell,
                        x,
                        y
                );
            }
        }
    }
}
