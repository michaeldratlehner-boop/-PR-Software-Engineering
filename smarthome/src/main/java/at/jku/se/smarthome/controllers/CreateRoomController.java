package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CreateRoomController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField sizeField;
    @FXML
    private ComboBox<String> roomComboBox;
    @FXML
    private ComboBox<String> roomLocationCombo;

    @FXML
    public void initialize() {
        // Raum-Typen
        roomComboBox.getItems().addAll(
                "Wohnzimmer",
                "Schlafzimmer",
                "Bad",
                "Küche",
                "Garage",
                "Keller",
                "Kinderzimmer",
                "Arbeitszimmer",
                "Esszimmer",
                "Freizeitraum",
                "Sonstiges"
        );

        // Position im Gebäude
        roomLocationCombo.getItems().addAll(
                "Erdgeschoss",
                "Obergeschoss",
                "Dachgeschoss",
                "Keller",
                "Sonstiges"
        );

        // Defaults
        roomComboBox.getSelectionModel().select("Wohnzimmer");
        roomLocationCombo.getSelectionModel().select("Erdgeschoss");
    }

    @FXML
    private void saveRoom() {
        String name = nameField.getText();
        String size = sizeField.getText();
        String type = roomComboBox.getValue();
        String location = roomLocationCombo.getValue();

        // vorläufig nur ausgeben (fake speichern)
        System.out.println("Neuer Raum:");
        System.out.println("Name:      " + name);
        System.out.println("Größe:     " + size + " m²");
        System.out.println("Zweck:     " + type);
        System.out.println("Position:  " + location);

        // zurück zum Dashboard
        App.setRoot("dashboard");
    }

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    @FXML
    private void cancel() {
        App.setRoot("dashboard");
    }
}