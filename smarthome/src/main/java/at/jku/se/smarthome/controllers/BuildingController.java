package at.jku.se.smarthome.controllers;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentHouse;
import at.jku.se.State.CurrentUser;
import at.jku.se.State.JsonStateService;
import at.jku.se.query.AppStateMutations;
import at.jku.se.query.AppStateQuery;
import at.jku.se.smarthome.App;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.service.HouseService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class BuildingController {
    private final AppState state = AppState.getInstance();
    private final AppStateQuery stateQuery = new AppStateQuery(state);
    private final HouseService houseService = new HouseService();
    private final AppStateMutations appStateMutations = new AppStateMutations(state);

    // --- Left card: house ---
    @FXML private VBox noHouseBox;
    @FXML private VBox hasHouseBox;

    @FXML private Label houseNameLabel;
    @FXML private Label houseFloorsLabel;
    @FXML private Label houseAddressLabel;
    @FXML private Label houseLocationLabel;

    // --- Right card: rooms in selected house ---
    @FXML private VBox roomsListBox;

    // --- Bottom card: unassigned rooms ---
    @FXML private VBox unassignedRoomsListBox;

    @FXML
    public void initialize() {
        refreshView();
    }

    private void refreshView() {
        User user = CurrentUser.getCurrentUser();
        if (user == null) {
            renderNoHouse();
            renderRoomsEmpty();
            renderUnassignedRoomsEmpty();
            return;
        }

        House house = null;
        String houseId = user.getHouseId();
        if (houseId != null && !houseId.isBlank()) {
            house = stateQuery.getHouse(houseId);
        }

        CurrentHouse.setCurrentHouse(house);

        if (house == null) {
            renderNoHouse();
        } else {
            renderHouse(house);
        }

        renderRoomsInHouse(state, house);
        renderUnassignedRooms(state);
    }

    // ----------------------------
    // House rendering
    // ----------------------------

    private void renderNoHouse() {
        if (noHouseBox != null) {
            noHouseBox.setVisible(true);
            noHouseBox.setManaged(true);
        }
        if (hasHouseBox != null) {
            hasHouseBox.setVisible(false);
            hasHouseBox.setManaged(false);
        }
    }

    private void renderHouse(House house) {
        if (noHouseBox != null) {
            noHouseBox.setVisible(false);
            noHouseBox.setManaged(false);
        }
        if (hasHouseBox != null) {
            hasHouseBox.setVisible(true);
            hasHouseBox.setManaged(true);
        }

        if (houseNameLabel != null) houseNameLabel.setText(nullSafe(house.getName()));
        if (houseFloorsLabel != null) houseFloorsLabel.setText(String.valueOf(house.getFloors()));
        if (houseAddressLabel != null) houseAddressLabel.setText(nullSafe(house.getAddress()));

        String lat = (house.getLatitude() == null) ? "-" : (house.getLatitude() + "°N");
        String lon = (house.getLongitude() == null) ? "-" : (house.getLongitude() + "°E");
        if (houseLocationLabel != null) houseLocationLabel.setText(lat + " / " + lon);
    }

    // ----------------------------
    // Rooms rendering
    // ----------------------------

    private void renderRoomsEmpty() {
        if (roomsListBox == null) return;
        roomsListBox.getChildren().clear();
        roomsListBox.getChildren().add(new Label("Noch keine Räume."));
    }

    private HBox createRoomRowWithEdit(Room room) {
        Label name = new Label(room.getName());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("Bearbeiten");
        editBtn.setOnAction(e -> openEditRoom(room));

        Button removeBtn = new Button("Entfernen");
        removeBtn.setOnAction(e -> deleteRoom(room));

        HBox row = new HBox(10, name, spacer, editBtn, removeBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void renderRoomsInHouse(AppState state, House house) {
        if (roomsListBox == null) return;

        roomsListBox.getChildren().clear();

        if (house == null) {
            roomsListBox.getChildren().add(new Label("Noch keine Räume."));
            return;
        }

        List<Room> rooms = stateQuery.getRoomsByHouseId(house.getId());
        if (rooms.isEmpty()) {
            roomsListBox.getChildren().add(new Label("Noch keine Räume."));
            return;
        }

        for (Room r : rooms) {
            roomsListBox.getChildren().add(createRoomRowWithEdit(r));
        }
    }

    private void renderUnassignedRoomsEmpty() {
        if (unassignedRoomsListBox == null) return;
        unassignedRoomsListBox.getChildren().clear();
        unassignedRoomsListBox.getChildren().add(new Label("Keine nicht zugeordneten Räume."));
    }

    private void renderUnassignedRooms(AppState state) {
        if (unassignedRoomsListBox == null) return;

        unassignedRoomsListBox.getChildren().clear();

        List<Room> unassigned = stateQuery.getAllRooms().stream()
                .filter(r -> r.getHouseId() == null || r.getHouseId().isBlank())
                .toList();

        if (unassigned.isEmpty()) {
            unassignedRoomsListBox.getChildren().add(new Label("Keine nicht zugeordneten Räume."));
            return;
        }

        for (Room r : unassigned) {
            unassignedRoomsListBox.getChildren().add(createUnassignedRowWithAssign(r));
        }
    }

    private HBox createUnassignedRowWithAssign(Room room) {
        Label name = new Label(room.getName());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button assignBtn = new Button("Zuordnen");
        assignBtn.setOnAction(e -> assignRoomToCurrentHouse(room));

        HBox row = new HBox(10, name, spacer, assignBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void assignRoomToCurrentHouse(Room room) {
        House house = CurrentHouse.getCurrentHouse();
        if (house == null) return;

        Room persistedRoom = stateQuery.getRoom(room.getId());
        if (persistedRoom == null) return;


        persistedRoom.setHouseId(house.getId());
        appStateMutations.saveRoom(persistedRoom);

        refreshView();
    }

    private void deleteRoom(Room room) {
        if (room == null) return;

        // Raum löschen (persistiert)
        appStateMutations.deleteRoom(room.getId());

        // Optional: falls dein Room evtl. Geräte enthält und du diese auch entfernen willst,
        // müsstest du das hier zusätzlich machen (nur falls dein Modell das so braucht).

        refreshView();
    }


    // ----------------------------
    // Actions from FXML
    // ----------------------------

    @FXML
    private void addHouse() {
        // Create-Mode: CurrentHouse = null => BuildingEditController geht auf CREATE
        CurrentHouse.clearCurrentHouse();
        App.setRoot("buildingEdit");
    }

    @FXML
    private void editHouse() {
        // Edit-Mode: CurrentHouse ist gesetzt
        App.setRoot("buildingEdit");
    }

    @FXML
    private void deleteHouse() {
        House house = CurrentHouse.getCurrentHouse();
        User user = CurrentUser.getCurrentUser();
        if (house == null || user == null) return;

        // 1) Haus löschen (Service)
        houseService.deleteHouse(house.getId());

        // 2) User entkoppeln (wichtig: houseId entfernen)
        User persisted = stateQuery.getUser(user.getId());
        if (persisted != null) {
            persisted.setHouseId(null);
            appStateMutations.saveUser(persisted);
        }

        CurrentHouse.clearCurrentHouse();
        refreshView();
    }

    @FXML
    private void openCreateRoom() {
        App.setRoot("createRoom");
    }

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    private static String nullSafe(String s) {
        return s == null ? "" : s;
    }
    private void openEditRoom(Room room) {
        App.setRoot("createRoom");
    }
    @FXML
    private void openShareHouse() {
        App.setRoot("shareHouse");
    }

}
