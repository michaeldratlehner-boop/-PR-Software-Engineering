package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class RoomCockpitController {

    @FXML private VBox roomsContainer;

    @FXML
    public void initialize() {
        // Beispiel-Daten, später durch echte Räume ersetzen
        roomsContainer.getChildren().clear();
        addRoomRow("Wohnen");
        addRoomRow("Schlafen");
        addRoomRow("Küche");
    }

    private void addRoomRow(String roomName) {
        HBox row = new HBox(8);
        row.setStyle("-fx-background-color:#f9f9f9; -fx-background-radius:20;"
                + "-fx-border-color:#cccccc; -fx-border-radius:20;");
        row.setPadding(new Insets(6, 6, 6, 16));

        Label nameLabel = new Label(roomName);
        nameLabel.setStyle("-fx-font-size:12;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Button editBtn = new Button("Bearbeiten");
        editBtn.setStyle("-fx-background-color:#333333; -fx-text-fill:white;"
                + "-fx-font-size:11; -fx-padding:4 14 4 14;");
        editBtn.setOnAction(e -> editRoom(roomName));

        Button deleteBtn = new Button("Löschen");
        deleteBtn.setStyle("-fx-background-color:white; -fx-text-fill:#333333;"
                + "-fx-border-color:#333333; -fx-border-radius:16;"
                + "-fx-font-size:11; -fx-padding:4 14 4 14;");
        deleteBtn.setOnAction(e -> deleteRoom(roomName, row));

        row.getChildren().addAll(nameLabel, editBtn, deleteBtn);
        roomsContainer.getChildren().add(row);
    }

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    // Wird vom Beispiel-Row in FXML nicht genutzt, aber von addRoomRow()
    @FXML
    private void editRoom() {
        // Platzhalter – ggf. Raum bearbeiten Page öffnen
        System.out.println("editRoom (ohne Parameter) – nur für SceneBuilder nötig");
    }

    private void editRoom(String roomName) {
        System.out.println("Raum bearbeiten: " + roomName);
        // TODO: Navigation zur „Raum bearbeiten“-Seite
    }

    @FXML
    private void deleteRoom() {
        // Platzhalter – nur für SceneBuilder, echte Logik unten
        System.out.println("deleteRoom (ohne Parameter)");
    }

    private void deleteRoom(String roomName, HBox row) {
        System.out.println("Raum löschen: " + roomName);
        roomsContainer.getChildren().remove(row);
        // TODO: Raum auch im Model/Backend löschen
    }
}
