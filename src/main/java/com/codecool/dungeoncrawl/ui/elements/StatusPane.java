package com.codecool.dungeoncrawl.ui.elements;

import com.codecool.dungeoncrawl.data.Item;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;

public class StatusPane {
    public static final int RIGHT_PANEL_WIDTH = 200;
    public static final int RIGHT_PANEL_PADDING = 10;
    private GridPane ui;
    private Label nameTextLabel;
    private Label nameValueLabel;
    private Label healthTextLabel;
    private Label healthValueLabel;
    private Label inventoryTextLabel;
    private Label inventoryValueLabel;

    public StatusPane() {
        ui = new GridPane();
        nameTextLabel = new Label("Name: ");
        nameValueLabel = new Label();
        healthTextLabel = new Label("Health: ");
        healthValueLabel = new Label();
        inventoryTextLabel = new Label("Inventory: ");
        inventoryValueLabel = new Label();
    }

    public BorderPane build() {
        ui.setPrefWidth(RIGHT_PANEL_WIDTH);
        ui.setPadding(new Insets(RIGHT_PANEL_PADDING));

        ui.add(nameTextLabel, 0, 0);
        ui.add(nameValueLabel, 1, 0);
        ui.add(healthTextLabel, 0, 1);
        ui.add(healthValueLabel, 1, 1);
        ui.add(inventoryTextLabel, 0, 1);
        ui.add(inventoryValueLabel, 1, 1);

        BorderPane borderPane = new BorderPane();
        borderPane.setRight(ui);
        return borderPane;
    }

    public void setHealthValue(String text) {
        healthValueLabel.setText(text);
    }

    public void setInventoryValue(List<Item> items) {
        String itemNames = items.stream()
                .map(Item::getName)
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
        inventoryValueLabel.setText(itemNames);
    }

    public void setNameValue(String text) {
        nameValueLabel.setText(text);
    }
}
