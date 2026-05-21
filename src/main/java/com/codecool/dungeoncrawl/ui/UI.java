package com.codecool.dungeoncrawl.ui;

//import com.codecool.dungeoncrawl.data.Chunk;
import com.codecool.dungeoncrawl.data.actors.Player;
import com.codecool.dungeoncrawl.logic.GameLogic;
import com.codecool.dungeoncrawl.ui.elements.MainStage;
import com.codecool.dungeoncrawl.ui.keyeventhandler.KeyHandler;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Set;

public class UI {
    private Canvas canvas;
    private GraphicsContext context;

    private MainStage mainStage;
    private GameLogic logic;
    private Renderer renderer;
    private Set<KeyHandler> keyHandlers;
    private Camera camera;
    private Stage stage;
    private boolean gameOverDialogShowing = false;

    public static final int CHUNK_SIZE = 16;

    public UI(GameLogic logic, Set<KeyHandler> keyHandlers) {
        this.logic = logic;
        this.keyHandlers = keyHandlers;

        this.canvas = new Canvas(
                CHUNK_SIZE * Tiles.TILE_WIDTH,
                CHUNK_SIZE * Tiles.TILE_WIDTH);

        this.context = canvas.getGraphicsContext2D();
        this.mainStage = new MainStage(canvas);
        this.camera = new Camera(
                (int)(canvas.getWidth() / Tiles.TILE_WIDTH),
                (int)(canvas.getHeight() / Tiles.TILE_WIDTH),
                logic.getMap()
        );

        this.renderer = new Renderer(context, logic, camera);
    }

    public void setUpPain(Stage primaryStage) {
        this.stage = primaryStage;
        Scene scene = mainStage.getScene();
        primaryStage.setScene(scene);
        logic.setup();
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        for (KeyHandler keyHandler : keyHandlers) {
            keyHandler.perform(keyEvent, logic.getMap());
        }
        logic.makeMonstersAct();
        refresh();
        showGameOverScreen();
    }

    public void refresh() {

        Player player = logic.getMap().getPlayer();

        renderer.getCamera().follow(player.getX(), player.getY());

        renderer.render();

        mainStage.setNameLabelText(logic.getPlayerName());
        mainStage.setHealthLabelText(logic.getPlayerHealth());
        mainStage.setInventoryLabelText(logic.getPlayerInventory());
        String equipment = logic.getMap().getPlayer().getEquippedArmor() != null ? "Armor" : "None";
        mainStage.setEquipmentLabelText(equipment);
    }

    private void showGameOverScreen() {
        if (!logic.isGameOver() || gameOverDialogShowing) {
            return;
        }

        gameOverDialogShowing = true;

        ButtonType tryAgainButton = new ButtonType("Try Again");
        ButtonType quitButton = new ButtonType("Quit Game");

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        alert.setContentText("Your health reached 0.");
        alert.getButtonTypes().setAll(tryAgainButton, quitButton);

        if (stage != null) {
            alert.initOwner(stage);
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == tryAgainButton) {
            logic.restartGame();
            gameOverDialogShowing = false;
            refresh();
            return;
        }

        Platform.exit();
    }
}
