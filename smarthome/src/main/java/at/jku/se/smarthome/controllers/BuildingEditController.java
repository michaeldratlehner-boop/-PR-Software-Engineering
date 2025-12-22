package at.jku.se.smarthome.controllers;

import at.jku.se.State.CurrentHouse;
import at.jku.se.State.CurrentUser;
import at.jku.se.smarthome.App;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.service.HouseService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BuildingEditController {

    private final HouseService houseService = new HouseService();

    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private TextField floorsField;
    @FXML private TextField addressField;

    @FXML private Label errorLabel;

    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;

    /** null = Create-Mode, sonst Edit-Mode */
    private House editingHouse;

    @FXML
    public void initialize() {

        // --- Info Card ---
        if (CurrentUser.getCurrentUser() != null) {
            userIdLabel.setText(CurrentUser.getCurrentUser().getId());
        }

        editingHouse = CurrentHouse.getCurrentHouse();

        if (editingHouse == null) {
            // CREATE
            titleLabel.setText("Gebäude erstellen");
            floorsField.setText("1");
            latitudeLabel.setText("-");
            longitudeLabel.setText("-");
        } else {
            // EDIT
            titleLabel.setText("Gebäude bearbeiten");
            nameField.setText(editingHouse.getName());
            addressField.setText(editingHouse.getAddress());
            floorsField.setText(String.valueOf(editingHouse.getFloors()));

            latitudeLabel.setText(
                    editingHouse.getLatitude() == null ? "-" : editingHouse.getLatitude() + "°N");
            longitudeLabel.setText(
                    editingHouse.getLongitude() == null ? "-" : editingHouse.getLongitude() + "°E");
        }

        hideError();
    }

    // ----------------------------
    // Floor Stepper
    // ----------------------------

    @FXML
    private void increaseFloors() {
        floorsField.setText(String.valueOf(parseFloors() + 1));
    }

    @FXML
    private void decreaseFloors() {
        floorsField.setText(String.valueOf(Math.max(1, parseFloors() - 1)));
    }

    // ----------------------------
    // Actions
    // ----------------------------

    @FXML
    private void save() {
        String name = safe(nameField.getText()).trim();
        String address = safe(addressField.getText()).trim();
        int floors = parseFloors();

        if (name.isBlank() || address.isBlank()) {
            showError("Bitte alle Pflichtfelder (*) ausfüllen.");
            return;
        }

        try {
            House result;

            if (editingHouse == null) {
                result = houseService.createHouse(name, floors, address);
            } else {
                result = houseService.updateHouse(
                        editingHouse.getId(), name, floors, address);
            }

            CurrentHouse.setCurrentHouse(result);
            App.setRoot("buildingCockpit");

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void cancel() {
        App.setRoot("buildingCockpit");
    }

    // ----------------------------
    // Helpers
    // ----------------------------

    private int parseFloors() {
        try {
            return Math.max(1, Integer.parseInt(floorsField.getText().trim()));
        } catch (Exception e) {
            return 1;
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
