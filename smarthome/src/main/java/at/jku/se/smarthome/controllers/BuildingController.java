package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;


public class BuildingController {

    public enum ViewMode {
        LIST,    // Liste der Gebäude
        CREATE,  // neues Gebäude anlegen
        EDIT     // bestehendes Gebäude bearbeiten (später)
    }

    // wird vor App.setRoot("building") von außen gesetzt
    public static ViewMode viewMode = ViewMode.LIST;

    // Popup-Overlay
    @FXML private VBox deletePopup;
    @FXML private Label deleteBuildingNameLabel;
    // die aktuell ausgewählte Zeile in der Liste
    private HBox rowToDelete;

    // --- List-View ---
    @FXML private VBox listCard;
    @FXML private VBox buildingsListBox;
    @FXML private Label exampleBuildingLabel;

    // --- Form-View ---
    @FXML private VBox formCard;
    @FXML private Label formTitleLabel;
    @FXML private Label formSubtitleLabel;

    @FXML private TextField nameField;
    @FXML private TextField floorsField;
    @FXML private TextField addressField;

    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;

    @FXML
    public void initialize() {
        // Dummy-Daten für Smart Home Informationen
        if (userIdLabel != null) {
            userIdLabel.setText("0e6422ba-98cc-41bb-9e4d-6b1e3b5ceb79");
        }
        if (latitudeLabel != null) {
            latitudeLabel.setText("48.158931");
        }
        if (longitudeLabel != null) {
            longitudeLabel.setText("14.018111");
        }

        if (floorsField != null &&
                (floorsField.getText() == null || floorsField.getText().isBlank())) {
            floorsField.setText("1");
        }

        if (exampleBuildingLabel != null) {
            exampleBuildingLabel.setText("Meine Wohnung");
        }

        updateView();
    }

    // -------- View-Umschalten --------

    private void updateView() {
        if (listCard == null || formCard == null) return;

        switch (viewMode) {
            case LIST -> showList();
            case CREATE -> showForm(false);
            case EDIT -> showForm(true);
        }
    }

    private void showList() {
        listCard.setVisible(true);
        listCard.setManaged(true);

        formCard.setVisible(false);
        formCard.setManaged(false);
    }

    private void showForm(boolean edit) {
        formCard.setVisible(true);
        formCard.setManaged(true);

        listCard.setVisible(false);
        listCard.setManaged(false);

        if (edit) {
            formTitleLabel.setText("Gebäude bearbeiten");
            formSubtitleLabel.setText("Bearbeiten Sie die Daten Ihres Gebäudes");
            // TODO: später hier das ausgewählte Gebäude in die Felder laden
        } else {
            formTitleLabel.setText("Gebäude erstellen");
            formSubtitleLabel.setText("Fügen Sie Ihr Haus oder Wohnung hinzu");
            clearForm();
        }
    }

    private void clearForm() {
        if (nameField != null) nameField.clear();
        if (floorsField != null) floorsField.setText("1");
        if (addressField != null) addressField.clear();
    }

    // -------- Buttons oben/unten --------

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    @FXML
    private void saveBuilding() {
        String name = nameField.getText();
        String floors = floorsField.getText();
        String address = addressField.getText();

        if (viewMode == ViewMode.EDIT) {
            System.out.println("Gebäude aktualisieren:");
        } else {
            System.out.println("Gebäude speichern:");
        }

        System.out.println("Name: " + name);
        System.out.println("Stockwerke: " + floors);
        System.out.println("Adresse: " + address);

        // Verhalten wie bisher:
        // nach Speichern zurück zum Dashboard
        App.setRoot("dashboard");
    }

    @FXML
    private void cancel() {
        // Abbrechen → wie bisher zurück zum Dashboard
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

    // -------- Aktionen in der Liste --------

    @FXML
    private void editSelectedBuilding() {
        System.out.println("Gebäude bearbeiten (Dummy)...");
        viewMode = ViewMode.EDIT;
        showForm(true);
    }

    @FXML
    private void shareSelectedBuilding() {
        System.out.println("Gebäude freigeben (Dummy)...");
    }

    @FXML
    private void deleteBuilding(ActionEvent event) {
        System.out.println("Gebäude löschen (Popup öffnen)...");

        // Zeile (HBox) ermitteln, aus der der Button geklickt wurde
        Button sourceButton = (Button) event.getSource();
        rowToDelete = (HBox) sourceButton.getParent();

        // Gebäudename: erstes Kind in der HBox ist Label
        Label nameLabel = (Label) rowToDelete.getChildren().get(0);
        String buildingName = nameLabel.getText();

        if (deleteBuildingNameLabel != null) {
            deleteBuildingNameLabel.setText(buildingName);
        }

        // Popup anzeigen
        deletePopup.setVisible(true);
        deletePopup.setManaged(true);
    }

    @FXML
    private void cancelDeleteBuilding() {
        // Popup schließen, nichts löschen
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }

    @FXML
    private void confirmDeleteBuilding() {
        if (rowToDelete != null) {
            // Aus der UI entfernen
            buildingsListBox.getChildren().remove(rowToDelete);

            // TODO: hier auch  json löschen
            System.out.println("Gebäude wirklich gelöscht.");
        }
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }


}

