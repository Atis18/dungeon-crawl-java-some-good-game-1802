package com.codecool.dungeoncrawl.ui;

import com.codecool.dungeoncrawl.data.Drawable;
import com.codecool.dungeoncrawl.data.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.MonsterTypes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("player", new Tile(27, 0));
        tileMap.put("skeleton_fighter", new Tile(29, 6));
        tileMap.put("skeleton_assassin", new Tile(30, 2));
        tileMap.put("skeleton_mage", new Tile(24, 0));
        tileMap.put("save", new Tile(26, 28));
        tileMap.put("load", new Tile(25, 28));
        tileMap.put("healpotion", new Tile(26, 22));
        tileMap.put("sword", new Tile(0, 28));
        tileMap.put("key", new Tile(16, 23));
        tileMap.put("bomb", new Tile(13, 31));
        tileMap.put("armor", new Tile(20, 21));
        tileMap.put("player_armored", new Tile(28, 0));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(getTileName(d));
        if (tile == null) {
            tile = tileMap.get("empty");
        }
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }

    private static String getTileName(Drawable drawable) {
        if (!(drawable instanceof Skeleton)) {
            return drawable.getTileName();
        }

        Skeleton skeleton = (Skeleton) drawable;
        MonsterTypes monsterType = skeleton.getMonsterType();

        switch (monsterType) {
            case ASSASSIN:
                return "skeleton_assassin";
            case MAGE:
                return "skeleton_mage";
            case FIGHTER:
            default:
                return "skeleton_fighter";
        }
    }
}
