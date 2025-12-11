package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class RoomController {

    //  wird von außen gesetzt (Dashboard / Liste)
    public static boolean editMode = false;

    //  Felder für createRoom.fxml
    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField sizeField;

    @FXML
    private ComboBox<String> roomComboBox;

    @FXML
    private ComboBox<String> roomLocationCombo;

    // Felder für roomCockpit.fxml
    @FXML
    private VBox roomsContainer;


    @FXML
    private VBox deletePopup;

    @FXML
    private Label deleteRoomNameLabel;

    private HBox rowToDelete;             // die gerade ausgewählte Zeile


    @FXML
    public void initialize() {

        // Formular-Seite (createRoom.fxml)
        if (titleLabel != null) {

            // ComboBoxen initialisieren
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

            roomLocationCombo.getItems().addAll(
                    "Erdgeschoss",
                    "Obergeschoss",
                    "Dachgeschoss",
                    "Keller",
                    "Sonstiges"
            );

            roomComboBox.getSelectionModel().select("Wohnzimmer");
            roomLocationCombo.getSelectionModel().select("Erdgeschoss");

            // Titel je nach Modus
            if (editMode) {
                titleLabel.setText("Raum bearbeiten");
                subtitleLabel.setText("Bearbeiten Sie die Eigenschaften des Raumes");

            } else {
                titleLabel.setText("Raum hinzufügen");
                subtitleLabel.setText("Fügen Sie Ihrem Haus oder Wohnung einen Raum hinzu");
            }
        }

        // -------- Raumliste-Seite (roomCockpit.fxml) --------
        if (roomsContainer != null) {
            // hier später Räume dynamisch ins VBox einfügen

        }
    }

    // createRoom-Actions

    @FXML
    private void saveRoom() {
        if (nameField == null) {

            return;
        }

        String name = nameField.getText();
        String size = sizeField.getText();
        String type = roomComboBox.getValue();
        String location = roomLocationCombo.getValue();

        if (editMode) {
            System.out.println("Raum bearbeiten:");
        } else {
            System.out.println("Neuer Raum:");
        }

        System.out.println("Name:      " + name);
        System.out.println("Größe:     " + size + " m²");
        System.out.println("Zweck:     " + type);
        System.out.println("Position:  " + location);

        // Navigation nach dem Speichern
        if (editMode) {
            App.setRoot("roomCockpit");
        } else {
            App.setRoot("dashboard");
        }
    }

    @FXML
    private void backToDashboard() {
        if (editMode) {
            App.setRoot("roomCockpit");
        } else {
            App.setRoot("dashboard");
        }
    }

    @FXML
    private void cancel() {
        if (editMode) {
            App.setRoot("roomCockpit");
        } else {
            App.setRoot("dashboard");
        }
    }

    // roomCockpit-Actions

    @FXML
    private void editRoom(ActionEvent event) {
        // später: hier noch den ausgewählten Raum merken
        editMode = true;
        App.setRoot("createRoom");   // selbe FXML wie beim Anlegen
    }


    // wird vom "Löschen"-Button in der Liste aufgerufen
    @FXML
    private void deleteRoom(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        rowToDelete = (HBox) sourceButton.getParent();   // die HBox-Zeile

        // Raumname (erstes Kind in der HBox)
        Label nameLabel = (Label) rowToDelete.getChildren().get(0);
        String roomName = nameLabel.getText();

        if (deleteRoomNameLabel != null) {
            deleteRoomNameLabel.setText(roomName);
        }

        // Popup einblenden
        deletePopup.setVisible(true);
        deletePopup.setManaged(true);
    }
    @FXML
    private void cancelDelete() {
        // Popup ausblenden, nichts löschen
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }

    @FXML
    private void confirmDelete() {
        if (rowToDelete != null) {
            // Zeile aus der UI entfernen
            roomsContainer.getChildren().remove(rowToDelete);
            // TODO: hier zusätzlich aus json löschen
        }
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }

}
