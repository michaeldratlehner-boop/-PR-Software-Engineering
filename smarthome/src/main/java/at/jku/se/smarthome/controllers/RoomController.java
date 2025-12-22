package at.jku.se.smarthome.controllers;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentUser;
import at.jku.se.State.JsonStateService;
import at.jku.se.smarthome.App;
import at.jku.se.smarthome.model.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import at.jku.se.State.CurrentRoom;
import java.util.List;
import java.util.UUID;

public class RoomController {

    /** wird von außen gesetzt (Dashboard / Liste) */
    public static boolean editMode = false;

    /** merkt sich den Raum fürs Bearbeiten */
    private static String selectedRoomId = null;

    // -------- createRoom.fxml --------
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;

    @FXML private TextField nameField;
    @FXML private TextField sizeField;

    @FXML private ComboBox<String> roomComboBox;
    @FXML private ComboBox<String> roomLocationCombo;

    // -------- roomCockpit.fxml --------
    @FXML private VBox roomsContainer;

    @FXML private VBox deletePopup;
    @FXML private Label deleteRoomNameLabel;

    private String roomIdToDelete = null;

    private final JsonStateService jsonStateService = JsonStateService.getInstance();

    @FXML
    public void initialize() {

        // ---------- createRoom.fxml ----------
        if (titleLabel != null) {
            // ComboBoxen initialisieren (wenn vorhanden)
            if (roomComboBox != null && roomComboBox.getItems().isEmpty()) {
                roomComboBox.getItems().addAll(
                        "Wohnzimmer", "Schlafzimmer", "Bad", "Küche", "Garage",
                        "Keller", "Kinderzimmer", "Arbeitszimmer", "Esszimmer",
                        "Freizeitraum", "Sonstiges"
                );
                roomComboBox.getSelectionModel().selectFirst();
            }

            if (roomLocationCombo != null && roomLocationCombo.getItems().isEmpty()) {
                roomLocationCombo.getItems().addAll(
                        "Erdgeschoss", "Obergeschoss", "Dachgeschoss", "Keller", "Sonstiges"
                );
                roomLocationCombo.getSelectionModel().selectFirst();
            }

            if (editMode) {
                titleLabel.setText("Raum bearbeiten");
                if (subtitleLabel != null) subtitleLabel.setText("Bearbeiten Sie die Eigenschaften des Raumes");
                prefillEditForm();
            } else {
                titleLabel.setText("Raum hinzufügen");
                if (subtitleLabel != null) subtitleLabel.setText("Fügen Sie Ihrem Haus oder Wohnung einen Raum hinzu");
            }
        }

        // ---------- roomCockpit.fxml ----------
        if (roomsContainer != null) {
            if (deletePopup != null) {
                deletePopup.setVisible(false);
                deletePopup.setManaged(false);
            }
            refreshRoomsList();
        }
    }

    // =========================================================
    // createRoom actions
    // =========================================================

    @FXML
    private void saveRoom() {
        if (nameField == null) return;

        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        String sizeText = sizeField != null && sizeField.getText() != null ? sizeField.getText().trim() : "";

        if (name.isBlank()) {
            System.out.println("Raumname darf nicht leer sein.");
            return;
        }

        if (CurrentUser.getCurrentUser() == null || CurrentUser.getCurrentUser().getHouseId() == null) {
            System.out.println("Kein Haus vorhanden – erst Gebäude anlegen.");
            return;
        }

        double size = 0.0;
        if (!sizeText.isBlank()) {
            try {
                size = Double.parseDouble(sizeText.replace(",", "."));
            } catch (Exception e) {
                System.out.println("Ungültige Größe: " + sizeText);
                return;
            }
        }

        AppState state = jsonStateService.load();

        // Raum anlegen/ändern
        Room room;
        if (editMode && selectedRoomId != null) {
            room = state.getRoom(selectedRoomId);
            if (room == null) {
                System.out.println("Raum zum Bearbeiten nicht gefunden.");
                return;
            }
        } else {
            room = new Room();
            room.setId(UUID.randomUUID().toString());
            room.setHouseId(CurrentUser.getCurrentUser().getHouseId());
        }

        room.setName(name);
        room.setSizeSquareMeters(size);

        state.saveRoom(room);

        System.out.println("Raum gespeichert: " + room.getName() + " (id=" + room.getId() + ")");

        // Navigation nach dem Speichern
        selectedRoomId = null;
        editMode = false;
        App.setRoot("rooms");
    }

    @FXML
    private void backToDashboard() {
        // du willst “Zurück” immer zum Dashboard:
        selectedRoomId = null;
        editMode = false;
        App.setRoot("dashboard");
    }

    @FXML
    private void cancel() {
        App.setRoot("buildingCockpit");
    }

    private void prefillEditForm() {
        if (selectedRoomId == null) return;

        AppState state = jsonStateService.load();
        Room room = state.getRoom(selectedRoomId);
        if (room == null) return;

        if (nameField != null) nameField.setText(room.getName() == null ? "" : room.getName());
        if (sizeField != null) sizeField.setText(Double.toString(room.getSizeSquareMeters()));
    }

    // =========================================================
    // roomCockpit actions + rendering
    // =========================================================

    private void refreshRoomsList() {
        if (roomsContainer == null) return;

        roomsContainer.getChildren().clear();

        if (CurrentUser.getCurrentUser() == null || CurrentUser.getCurrentUser().getHouseId() == null) {
            roomsContainer.getChildren().add(new Label("Kein Haus vorhanden – zuerst Gebäude anlegen."));
            return;
        }

        AppState state = jsonStateService.load();
        String houseId = CurrentUser.getCurrentUser().getHouseId();

        List<Room> rooms = state.getRoomsByHouseId(houseId);

        if (rooms.isEmpty()) {
            roomsContainer.getChildren().add(new Label("Noch keine Räume."));
            return;
        }

        for (Room r : rooms) {
            roomsContainer.getChildren().add(createRoomRow(r));
        }
    }

    private HBox createRoomRow(Room room) {

        Button open = new Button("Öffnen");
        open.setStyle("-fx-background-color:white; -fx-text-fill:#333333;" +
                " -fx-border-color:#cccccc; -fx-border-radius:4; -fx-padding:4 14 4 14;");
        open.setOnAction(e -> {
            CurrentRoom.setCurrentRoom(room);
            App.setRoot("roomCockpit");
        });

        Label name = new Label(room.getName() == null ? "(ohne Name)" : room.getName());
        name.setStyle("-fx-font-size:13; -fx-font-weight:bold; -fx-text-fill:#222;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button edit = new Button("Bearbeiten");
        edit.setStyle("-fx-background-color:#333333; -fx-text-fill:white; -fx-padding:4 14 4 14;");
        edit.setUserData(room.getId());
        edit.setOnAction(this::editRoom);

        Button delete = new Button("Löschen");
        delete.setStyle("-fx-background-color:white; -fx-text-fill:#333333;" +
                " -fx-border-color:#cccccc; -fx-border-radius:4; -fx-padding:4 14 4 14;");
        delete.setUserData(room.getId());
        delete.setOnAction(this::deleteRoom);

        HBox row = new HBox(10, name, spacer, edit, delete);
        row.setStyle("-fx-background-color:white; -fx-padding:10; -fx-background-radius:15;");
        return row;
    }

    @FXML
    private void editRoom(ActionEvent event) {
        Object id = ((Button) event.getSource()).getUserData();
        if (id == null) return;

        selectedRoomId = id.toString();
        editMode = true;
        App.setRoot("createRoom"); // öffnet Formular im Edit-Mode (prefill in initialize)
    }

    @FXML
    private void deleteRoom(ActionEvent event) {
        Object id = ((Button) event.getSource()).getUserData();
        if (id == null) return;

        roomIdToDelete = id.toString();

        AppState state = jsonStateService.load();
        Room r = state.getRoom(roomIdToDelete);

        if (deleteRoomNameLabel != null) {
            deleteRoomNameLabel.setText(r != null && r.getName() != null ? r.getName() : "");
        }

        if (deletePopup != null) {
            deletePopup.setVisible(true);
            deletePopup.setManaged(true);
        }
    }

    @FXML
    private void cancelDelete() {
        roomIdToDelete = null;
        if (deletePopup != null) {
            deletePopup.setVisible(false);
            deletePopup.setManaged(false);
        }
    }

    @FXML
    private void confirmDelete() {
        if (roomIdToDelete == null) {
            cancelDelete();
            return;
        }

        AppState state = jsonStateService.load();
        state.deleteRoom(roomIdToDelete);

        roomIdToDelete = null;
        if (deletePopup != null) {
            deletePopup.setVisible(false);
            deletePopup.setManaged(false);
        }

        refreshRoomsList(); // <-- Liste sofort neu laden
    }

    public static void selectRoomForEdit(String roomId) {
        selectedRoomId = roomId;
    }

}
