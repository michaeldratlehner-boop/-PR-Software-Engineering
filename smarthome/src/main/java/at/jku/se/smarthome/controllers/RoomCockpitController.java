package at.jku.se.smarthome.controllers;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentHouse;
import at.jku.se.State.CurrentRoom;
import at.jku.se.State.CurrentUser;
import at.jku.se.State.JsonStateService;
import at.jku.se.query.AppStateMutations;
import at.jku.se.query.AppStateQuery;
import at.jku.se.smarthome.App;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.devices.SmartDevice;
import at.jku.se.smarthome.service.DeviceService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class RoomCockpitController {

    private final DeviceService deviceService = new DeviceService();
    private final AppState state = AppState.getInstance();
    private final AppStateMutations appStateMutations = new AppStateMutations(state);
    private final AppStateQuery stateQuery = new AppStateQuery(state);

    // ====== DETAIL (roomCockpit.fxml) ======
    @FXML private Label roomNameLabel;
    @FXML private Label roomSizeLabel;
    @FXML private Label roomHouseLabel;

    @FXML private VBox assignedDevicesBox;
    @FXML private VBox availableDevicesBox;

    // ====== LIST (rooms.fxml) ======
    @FXML private VBox roomsBox;
    @FXML private Label emptyLabel;

    private Room room;

    @FXML
    public void initialize() {
        JsonStateService.getInstance().load();

        // Wenn rooms.fxml geladen wurde, sind roomsBox/emptyLabel vorhanden
        if (roomsBox != null) {
            loadRoomsList();
            return;
        }

        // Wenn roomCockpit.fxml geladen wurde, sind die Detail-Labels vorhanden
        room = CurrentRoom.getCurrentRoom();
        if (room == null) {
            App.setRoot("rooms"); // zurück zur Liste
            return;
        }

        refreshDetail();
    }

    // =======================
    // LIST LOGIC (rooms.fxml)
    // =======================

    private void loadRoomsList() {
        roomsBox.getChildren().clear();


        String houseId = (CurrentUser.getCurrentUser() != null) ? CurrentUser.getCurrentUser().getHouseId() : null;
        if (houseId == null || houseId.isBlank()) {
            showEmpty("Kein Gebäude vorhanden. Bitte zuerst ein Gebäude anlegen.");
            return;
        }

        // CurrentHouse optional korrekt setzen
        House h = stateQuery.getHouse(houseId);
        CurrentHouse.setCurrentHouse(h);

        List<Room> rooms = stateQuery.getRoomsByHouseId(houseId);
        if (rooms.isEmpty()) {
            showEmpty("Keine Räume vorhanden.");
            return;
        }

        hideEmpty();
        for (Room r : rooms) {
            roomsBox.getChildren().add(roomRow(r));
        }
    }

    private HBox roomRow(Room r) {
        Label title = new Label(r.getName() == null ? "-" : r.getName());
        title.setStyle("-fx-font-size:13; -fx-text-fill:#222; -fx-font-weight:bold;");

        Label sub = new Label(String.format("%.1f m²", r.getSizeSquareMeters()));
        sub.setStyle("-fx-font-size:11; -fx-text-fill:#666;");

        VBox left = new VBox(2, title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button edit = new Button("Bearbeiten");
        edit.setOnAction(e -> {
            CurrentRoom.setCurrentRoom(r);
            RoomController.editMode = true;
            App.setRoot("createRoom");
        });

        Button delete = new Button("Löschen");
        delete.setStyle("-fx-background-color:white; -fx-text-fill:#333333;" +
                " -fx-border-color:#cccccc; -fx-border-radius:4; -fx-padding:3 10 3 10;");
        delete.setOnAction(e -> deleteRoomFromList(r));

        Button open = new Button("Öffnen");
        open.setStyle("-fx-background-color:#333333; -fx-text-fill:white; -fx-padding:3 10 3 10;");
        open.setOnAction(e -> {
            CurrentRoom.setCurrentRoom(r);
            App.setRoot("roomCockpit");
        });

        HBox row = new HBox(10, left, spacer, edit, delete, open);
        row.setStyle("-fx-background-color:white; -fx-padding:10; -fx-background-radius:15;");
        return row;
    }


    private void showEmpty(String msg) {
        if (emptyLabel != null) {
            emptyLabel.setText(msg);
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
        }
    }

    private void hideEmpty() {
        if (emptyLabel != null) {
            emptyLabel.setVisible(false);
            emptyLabel.setManaged(false);
        }
    }

    @FXML
    private void createRoom() {
        RoomController.editMode = false;
        CurrentRoom.setCurrentRoom(null);
        App.setRoot("createRoom");
    }

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    // ==========================
    // DETAIL LOGIC (roomCockpit)
    // ==========================

    private void refreshDetail() {

        Room latest = stateQuery.getRoom(room.getId());
        if (latest != null) {
            room = latest;
            CurrentRoom.setCurrentRoom(room);
        }

        roomNameLabel.setText(room.getName() == null ? "-" : room.getName());
        roomSizeLabel.setText(room.getSizeSquareMeters() + " m²");

        String hid = room.getHouseId();
        if (hid != null && !hid.isBlank()) {
            House h = stateQuery.getHouse(hid);
            roomHouseLabel.setText(h != null && h.getName() != null ? h.getName() : "-");
        } else {
            roomHouseLabel.setText("-");
        }

        renderAssigned();
        renderAvailable();
    }

    private void renderAssigned() {
        assignedDevicesBox.getChildren().clear();
        for (SmartDevice d : deviceService.getDevicesInRoom(room.getId())) {
            assignedDevicesBox.getChildren().add(rowAssigned(d));
        }
        if (assignedDevicesBox.getChildren().isEmpty()) {
            assignedDevicesBox.getChildren().add(new Label("Keine Geräte im Raum."));
        }
    }

    private void renderAvailable() {
        availableDevicesBox.getChildren().clear();
        for (SmartDevice d : deviceService.getUnassignedDevices()) {
            availableDevicesBox.getChildren().add(rowAvailable(d));
        }
        if (availableDevicesBox.getChildren().isEmpty()) {
            availableDevicesBox.getChildren().add(new Label("Keine verfügbaren Geräte."));
        }
    }

    private HBox rowAssigned(SmartDevice d) {
        Label name = new Label(d.getName() + " (" + d.getClass().getSimpleName() + ")");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button remove = new Button("Entfernen");
        remove.setOnAction(e -> {
            deviceService.unassignDevice(d.getId());
            refreshDetail();
        });

        HBox row = new HBox(10, name, spacer, remove);
        row.setStyle("-fx-background-color:white; -fx-padding:10; -fx-background-radius:15;");
        return row;
    }

    private HBox rowAvailable(SmartDevice d) {
        Label name = new Label(d.getName() + " (" + d.getClass().getSimpleName() + ")");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button assign = new Button("Zuweisen");
        assign.setOnAction(e -> {
            deviceService.assignDeviceToRoom(d.getId(), room.getId());
            refreshDetail();
        });

        HBox row = new HBox(10, name, spacer, assign);
        row.setStyle("-fx-background-color:white; -fx-padding:10; -fx-background-radius:15;");
        return row;
    }

    @FXML
    private void editRoom() {
        CurrentRoom.setCurrentRoom(room);
        RoomController.editMode = true;
        App.setRoot("createRoom");
    }

    @FXML
    private void deleteRoom() {
        appStateMutations.deleteRoom(room.getId());
        CurrentRoom.setCurrentRoom(null);
        App.setRoot("rooms");
    }

    @FXML
    private void openCreateDevice() {
        DeviceController.viewMode = DeviceController.ViewMode.CREATE;
        App.setRoot("deviceCockpit");
    }

    @FXML
    private void back() {
        App.setRoot("rooms");
    }

    private void deleteRoomFromList(Room r) {
        // Geräte, die in diesem Raum sind -> roomId entfernen (sonst "hängende" Zuordnung)
        if(r == null) return;
        appStateMutations.deleteRoom(r.getId());
        // UI neu laden
        loadRoomsList();
    }

}
