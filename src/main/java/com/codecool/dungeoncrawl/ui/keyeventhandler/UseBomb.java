package com.codecool.dungeoncrawl.ui.keyeventhandler;

import com.codecool.dungeoncrawl.data.GameMap;
import com.codecool.dungeoncrawl.data.Item;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.data.items.Bomb;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class UseBomb implements KeyHandler {
    public static final KeyCode code = KeyCode.B;

    @Override
    public void perform(KeyEvent event, GameMap map) {
        if (!code.equals(event.getCode())) return;

        Player player = map.getPlayer();
        Bomb bomb = null;

        for (Item item : player.getInventory()) {
            if (item instanceof Bomb) {
                bomb = (Bomb) item;
                break;
            }
        }

        if (bomb != null) {
            bomb.use(player);
            player.getInventory().remove(bomb);
        }
    }
}
