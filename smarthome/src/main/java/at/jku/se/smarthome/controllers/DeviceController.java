package at.jku.se.smarthome.controllers;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentRoom;
import at.jku.se.State.JsonStateService;
import at.jku.se.query.AppStateMutations;
import at.jku.se.query.AppStateQuery;
import at.jku.se.smarthome.App;
import at.jku.se.smarthome.factory.DeviceFactory;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.devices.SmartDevice;
import at.jku.se.smarthome.model.devices.actors.Actor;
import at.jku.se.smarthome.model.devices.sensors.LightSensor;
import at.jku.se.smarthome.model.devices.sensors.LightSensorType;
import at.jku.se.smarthome.model.devices.sensors.Sensor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DeviceController {

    public enum ViewMode { LIST, CREATE, EDIT }
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
    private String deviceIdToDelete;

    // (optional) gemerktes Device für EDIT/OPEN
    private String selectedDeviceId;

    private final AppState state = AppState.getInstance();
    private final AppStateQuery q = new AppStateQuery(state);
    private final AppStateMutations m = new AppStateMutations(state);

    @FXML
    public void initialize() {
        if (exampleDeviceLabel != null) {
            exampleDeviceLabel.setText("TemperaturSensor_01");
        }

        if (deviceKindCombo != null) {
            deviceKindCombo.getItems().setAll(
                    "Temperatursensor",
                    "Lichtsensor",
                    "Bewegungsmelder",
                    "Rauchmelder",
                    "Steckdose",
                    "Lichtsteuerung",
                    "Rolladensteuerung",
                    "Heizungssteuerung",
                    "Türsteuerung",
                    "Alarmsystem"
            );
        }

        // falls deletePopup im FXML fehlt oder noch nicht genutzt wird:
        if (deletePopup != null) {
            deletePopup.setVisible(false);
            deletePopup.setManaged(false);
        }

        updateView();
        refreshDeviceList();
    }

    // ---------------- View Umschalten ----------------

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

        refreshDeviceList();
    }

    private void showForm(boolean edit) {
        formCard.setVisible(true);
        formCard.setManaged(true);

        listCard.setVisible(false);
        listCard.setManaged(false);

        if (edit) {
            formTitleLabel.setText("Gerät/Sensor bearbeiten");
            formSubtitleLabel.setText("Bearbeiten Sie die Eigenschaften des Geräts/Sensors");
            // TODO: ausgewähltes Gerät in Felder laden (wenn du Edit wirklich brauchst)
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

    // ---------------- Navigation ----------------

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    @FXML
    private void createDevice() {
        viewMode = ViewMode.CREATE;
        updateView();
    }

    @FXML
    private void cancel() {
        // für Tests: zurück zur Liste, nicht Dashboard
        viewMode = ViewMode.LIST;
        updateView();
    }

    // ---------------- Speichern (wichtig für Counter + Liste) ----------------

    @FXML
    private void saveDevice() {
        String name = deviceNameField.getText();
        String kind = deviceKindCombo.getValue();

        if (name == null || name.isBlank() || kind == null || kind.isBlank()) {
            System.out.println("Bitte Name und Geräteart auswählen!");
            return;
        }

        double settedTemp = 22.0;
        LightSensor lightSensor = null;
        double brightnessVerge = 200;
        LightSensorType lightSensorType = LightSensorType.OUTDOOR;

        // ✅ immer unassigned anlegen
        String roomId = null;

        SmartDevice device = DeviceFactory.create(
                kind,
                name,
                roomId,
                settedTemp,
                lightSensor,
                brightnessVerge,
                lightSensorType
        );

        if (device instanceof Sensor sensor) {
            m.saveSensor(sensor);
            System.out.println("Sensor gespeichert: " + sensor.getName());
        } else if (device instanceof Actor actor) {
            m.saveActor(actor);
            System.out.println("Aktor gespeichert: " + actor.getName());
        } else {
            System.out.println("Unbekannter Gerätetyp");
            return;
        }

        // ✅ Persistieren
        JsonStateService.getInstance().save(state);

        // ✅ Wenn wir aus einem Raum heraus "Gerät hinzufügen" gedrückt haben:
        // zurück ins RoomCockpit, damit es sofort in "Verfügbare Geräte" auftaucht.
        if (CurrentRoom.getCurrentRoom() != null) {
            App.setRoot("roomCockpit");
            return;
        }

        // sonst zurück zur Liste im DeviceCockpit
        viewMode = ViewMode.LIST;
        updateView();
    }

    // ---------------- Liste Actions ----------------

    @FXML
    private void editSelectedDevice() {
        // TODO: selectedDeviceId setzen und Felder befüllen
        viewMode = ViewMode.EDIT;
        updateView();
    }

    @FXML
    private void openDevice(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        HBox row = (HBox) sourceButton.getParent();

        Object id = row.getUserData();
        selectedDeviceId = (id instanceof String s) ? s : null;

        System.out.println("openDevice clicked, id=" + selectedDeviceId);
        // TODO: hier ggf. Device-Detail / Cockpit öffnen
    }

    @FXML
    private void deleteDevice(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        rowToDelete = (HBox) sourceButton.getParent();

        Object id = rowToDelete.getUserData();
        deviceIdToDelete = (id instanceof String s) ? s : null;

        // Name anzeigen (aus VBox -> Label)
        String deviceName = "";
        if (!rowToDelete.getChildren().isEmpty() && rowToDelete.getChildren().get(0) instanceof VBox left) {
            if (!left.getChildren().isEmpty() && left.getChildren().get(0) instanceof Label nameLabel) {
                deviceName = nameLabel.getText();
            }
        }

        if (deleteDeviceNameLabel != null) {
            deleteDeviceNameLabel.setText(deviceName);
        }

        if (deletePopup != null) {
            deletePopup.setVisible(true);
            deletePopup.setManaged(true);
        }
    }

    @FXML
    private void cancelDeleteDevice() {
        if (deletePopup != null) {
            deletePopup.setVisible(false);
            deletePopup.setManaged(false);
        }
        rowToDelete = null;
        deviceIdToDelete = null;
    }

    @FXML
    private void confirmDeleteDevice() {
        if (deviceIdToDelete != null) {
            // aus State entfernen
            if (state.getSensors().remove(deviceIdToDelete) != null) {
                System.out.println("Sensor entfernt: " + deviceIdToDelete);
            } else if (state.getActors().remove(deviceIdToDelete) != null) {
                System.out.println("Aktor entfernt: " + deviceIdToDelete);
            }
            JsonStateService.getInstance().save(state);
        }

        if (deletePopup != null) {
            deletePopup.setVisible(false);
            deletePopup.setManaged(false);
        }

        rowToDelete = null;
        deviceIdToDelete = null;

        refreshDeviceList();
    }

    // ---------------- Liste neu aufbauen ----------------

    private void refreshDeviceList() {
        if (devicesListBox == null) return;

        devicesListBox.getChildren().clear();

        state.getSensors().values().forEach(d -> devicesListBox.getChildren().add(buildRow(d, "Sensor")));
        state.getActors().values().forEach(d -> devicesListBox.getChildren().add(buildRow(d, "Aktor")));
    }

    private HBox buildRow(SmartDevice device, String subText) {
        String roomName = resolveRoomNameInline(device);

        Label name = new Label(device.getName() + " (" + roomName + ")");
        name.setStyle("-fx-font-size:13; -fx-font-weight:bold; -fx-text-fill:#222222;");

        Label type = new Label(subText);
        type.setStyle("-fx-font-size:11; -fx-text-fill:#666666;");

        VBox left = new VBox(2, name, type);
        left.setAlignment(Pos.CENTER_LEFT);

        Button edit = new Button("Bearbeiten");
        Button delete = new Button("Löschen");
        Button open = new Button("Öffnen");

        String btnStyle =
                "-fx-background-color:#efefef;" +
                        "-fx-text-fill:#222222;" +
                        "-fx-border-color:#cfcfcf;" +
                        "-fx-border-radius:4;" +
                        "-fx-background-radius:4;" +
                        "-fx-padding:4 12 4 12;";

        edit.setStyle(btnStyle);
        delete.setStyle(btnStyle);
        open.setStyle(btnStyle);

        edit.setOnAction(e -> editSelectedDevice());
        delete.setOnAction(this::deleteDevice);
        open.setOnAction(this::openDevice);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(12, left, spacer, edit, delete, open);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-background-color:white;" +
                        "-fx-background-radius:16;" +
                        "-fx-border-radius:16;" +
                        "-fx-border-color:#cccccc;"
        );
        row.setPadding(new Insets(10, 14, 10, 16));

        // Device-ID merken
        row.setUserData(device.getId());
        Label roomLine = new Label(resolveRoomNameInline(device));

        return row;
    }
    private String resolveRoomNameInline(SmartDevice device) {
        String rid = device.getRoomId();
        if (rid == null || rid.isBlank()) return "noch nicht zugewiesen";

        Room r = state.getRooms().get(rid);
        if (r == null) return "noch nicht zugewiesen";
        return r.getName();
    }


}
