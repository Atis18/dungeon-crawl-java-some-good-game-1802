package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.data.database.DatabaseManager;
import com.codecool.dungeoncrawl.ui.UI;
import com.codecool.dungeoncrawl.ui.keyeventhandler.*;
import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Set;

public class Game extends Application {
    private UI ui;
    private GameLogic logic;
    private Set<KeyHandler> keyHandlers;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.initializeDatabase();

        this.keyHandlers = Set.of(new Up(), new Down(), new Left(), new Right(), new Heal());
        this.logic = new GameLogic();
        logic.setPlayerName(askPlayerName());
        this.ui = new UI(logic, keyHandlers);
        ui.setUpPain(primaryStage);

        primaryStage.setTitle(logic.getPlayerName() + " - Dungeon Crawl");
        primaryStage.show();
    }

    private String askPlayerName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Player Name");
        dialog.setHeaderText("Choose your player name");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
