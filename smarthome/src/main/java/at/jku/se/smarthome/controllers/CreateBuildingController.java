package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateBuildingController {

    @FXML private TextField nameField;
    @FXML private TextField floorsField;
    @FXML private TextField addressField;

    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;

    @FXML
    private void backToDashboard() {
        // Zurück zum Dashboard
        App.setRoot("dashboard");
    }

    @FXML
    private void saveBuilding() {
        // Vorläufig nur "fake" speichern
        String name = nameField.getText();
        String floors = floorsField.getText();
        String address = addressField.getText();

        System.out.println("Gebäude speichern:");
        System.out.println("Name: " + name);
        System.out.println("Stockwerke: " + floors);
        System.out.println("Adresse: " + address);

        // Danach wieder auf Dashboard
        App.setRoot("dashboard");
    }

    @FXML
    private void cancel() {
        // Abbrechen → zurück zum Dashboard
        App.setRoot("dashboard");
    }

    @FXML
    private void increaseFloors() {
        int current = parseFloors();
        floorsField.setText(String.valueOf(current + 1));
    }

    @FXML
    private void decreaseFloors() {
        int current = parseFloors();
        if (current > 1) {
            floorsField.setText(String.valueOf(current - 1));
        }
    }

    private int parseFloors() {
        try {
            return Integer.parseInt(floorsField.getText().trim());
        } catch (Exception e) {
            return 1;
        }
    }

    @FXML
    public void initialize() {
        // Dummy-Daten für Smart Home Informationen
        userIdLabel.setText("0e6422ba-98cc-41bb-9e4d-6b1e3b5ceb79");
        latitudeLabel.setText("48.158931");
        longitudeLabel.setText("14.018111");

        if (floorsField.getText() == null || floorsField.getText().isBlank()) {
            floorsField.setText("1");
        }
    }
}
