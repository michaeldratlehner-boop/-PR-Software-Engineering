package at.jku.se.smarthome.controllers;


import at.jku.se.smarthome.App;
import at.jku.se.smarthome.factory.DeviceFactory;
import at.jku.se.smarthome.model.devices.SmartDevice;
import at.jku.se.smarthome.model.devices.actors.Actor;
import at.jku.se.smarthome.model.devices.sensors.LightSensor;
import at.jku.se.smarthome.model.devices.sensors.LightSensorType;
import at.jku.se.smarthome.model.devices.sensors.Sensor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import at.jku.se.State.*;

public class DeviceController {

    public enum ViewMode {
        LIST,
        CREATE,
        EDIT
    }

    public static ViewMode viewMode = ViewMode.LIST;

    // List-View
    @FXML private VBox listCard;
    @FXML private VBox devicesListBox;
    @FXML private Label exampleDeviceLabel;

    // Form-View
    @FXML private VBox formCard;
    @FXML private Label formTitleLabel;
    @FXML private Label formSubtitleLabel;

    @FXML private TextField deviceNameField;
    @FXML private TextField deviceTypeField;
    @FXML private ComboBox<String> deviceKindCombo;
    @FXML private PasswordField wifiPasswordField;

    // Delete-Popup
    @FXML private VBox deletePopup;
    @FXML private Label deleteDeviceNameLabel;
    private HBox rowToDelete;

    @FXML
    public void initialize() {

        // Beispielgerät für die Liste
        if (exampleDeviceLabel != null) {
            exampleDeviceLabel.setText("TemperaturSensor_01");
        }

        // Gerät/Sensor Auswahl (ComboBox)
        if (deviceKindCombo != null) {
            deviceKindCombo.getItems().setAll(
                    "Temperatursensor",
                    "Lichtsensor",
                    "Bewegungsmelder",
                    "Steckdose",
                    "Lichtsteuerung",
                    "Türsteuerung",
                    "Heizungssteuerung",
                    "Rolladensteuerung",
                    "Lichtsteuerung",
                    "Alarmsystem",
                    "Rauchmelder"
            );
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
            formTitleLabel.setText("Gerät/Sensor bearbeiten");
            formSubtitleLabel.setText("Bearbeiten Sie die Eigenschaften des Geräts/Sensors");
            // TODO: ausgewähltes Gerät laden
        } else {
            formTitleLabel.setText("Gerät/Sensor hinzufügen");
            formSubtitleLabel.setText("Fügen Sie Ihrem Haus oder Ihrer Wohnung ein Gerät/Sensor hinzu");
            clearForm();
        }
    }

    private void clearForm() {
        if (deviceNameField != null) deviceNameField.clear();
        if (deviceTypeField != null) deviceTypeField.clear();
        if (deviceKindCombo != null) deviceKindCombo.getSelectionModel().clearSelection();
        if (wifiPasswordField != null) wifiPasswordField.clear();
    }

    // -------- Navigation oben/unten --------

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    @FXML
    private void saveDevice() {
        String name = deviceNameField.getText();
        String type = deviceTypeField.getText();
        String kind = deviceKindCombo.getValue();
        String wifi = wifiPasswordField.getText();

        double settedTemp = 22.0;
        LightSensor lightSensor = null;
        double brightnessVerge = 200;
        LightSensorType lightSensorType = LightSensorType.OUTDOOR;
        String roomId = (at.jku.se.State.CurrentRoom.getCurrentRoom() != null)
                ? at.jku.se.State.CurrentRoom.getCurrentRoom().getId()
                : null;


        SmartDevice device = DeviceFactory.create(
                kind,
                name,
                roomId,
                settedTemp,
                lightSensor,
                brightnessVerge,
                lightSensorType
        );

        if(device instanceof Sensor sensor) {
            AppState.getInstance().saveSensor(sensor);
            System.out.println("Sensor gespeichert: " + sensor.getName());
        } else if(device instanceof Actor actor) {
            AppState.getInstance().saveActor(actor);
            System.out.println("Aktor gespeichert: " + actor.getName());
        } else {
            System.out.println("Unbekannter Gerätetyp");
        }

        if (viewMode == ViewMode.EDIT) {
            System.out.println("Gerät aktualisieren:");
        } else {
            System.out.println("Gerät speichern:");
        }

        System.out.println("Name: " + name);
        System.out.println("Typ: " + type);
        System.out.println("Auswahl: " + kind);
        System.out.println("WLAN-Passwort: " + wifi);

        if (viewMode == ViewMode.EDIT) {
            viewMode = ViewMode.LIST;
            updateView();
        } else {
            App.setRoot("dashboard");
        }
    }

    @FXML
    private void cancel() {
        if (viewMode == ViewMode.EDIT) {
            viewMode = ViewMode.LIST;
            updateView();
        } else {
            App.setRoot("dashboard");
        }
    }

    // -------- Aktionen in der Liste --------

    @FXML
    private void editSelectedDevice() {
        // später: ausgewähltes Gerät laden
        viewMode = ViewMode.EDIT;
        showForm(true);
    }

    @FXML
    private void deleteDevice(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        rowToDelete = (HBox) sourceButton.getParent();

        Label nameLabel = (Label) rowToDelete.getChildren().get(0);
        String deviceName = nameLabel.getText();

        if (deleteDeviceNameLabel != null) {
            deleteDeviceNameLabel.setText(deviceName);
        }

        deletePopup.setVisible(true);
        deletePopup.setManaged(true);
    }

    @FXML
    private void cancelDeleteDevice() {
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }

    @FXML
    private void confirmDeleteDevice() {
        if (rowToDelete != null) {
            devicesListBox.getChildren().remove(rowToDelete);
            // TODO: aus Datenmodell entfernen
            System.out.println("Gerät gelöscht.");
        }
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }
}
