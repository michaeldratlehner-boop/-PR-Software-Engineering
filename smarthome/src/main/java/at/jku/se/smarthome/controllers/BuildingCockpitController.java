package at.jku.se.smarthome.controllers;

import javafx.event.ActionEvent;
import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class BuildingCockpitController {

    @FXML
    private VBox buildingsListBox;

    @FXML
    private Label exampleBuildingLabel;

    @FXML
    public void initialize() {
        // Hier später echte Gebäude laden und in buildingsListBox einfügen.
        // exampleBuildingLabel ist nur ein Platzhalter.
        exampleBuildingLabel.setText("Meine Wohnung");
    }

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    @FXML
    private void editSelectedBuilding() {
        System.out.println("Gebäude bearbeiten (Dummy)...");
        // z.B. App.setRoot("editBuilding");
    }

    @FXML
    private void shareSelectedBuilding() {
        System.out.println("Gebäude freigeben (Dummy)...");
    }
}
