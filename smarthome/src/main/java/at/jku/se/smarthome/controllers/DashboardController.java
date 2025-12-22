package at.jku.se.smarthome.controllers;

import at.jku.se.State.AppState;
import at.jku.se.State.JsonStateService;
import at.jku.se.smarthome.App;
import at.jku.se.smarthome.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import at.jku.se.State.CurrentUser;
import javafx.scene.layout.VBox;

public class DashboardController {
    @FXML private Label usernameLabel;
    @FXML private Label roomsCount;
    @FXML private Label devicesCount;
    @FXML private Label rulesCount;
    @FXML private Label sensorsCount;
    @FXML private Label buildingsCount;
    @FXML private VBox addBuildingCard;

    @FXML
    public void initialize() {
        User u = CurrentUser.getCurrentUser();
        if (u != null) {
            usernameLabel.setText(u.getFirstName() + " " + u.getLastName());
        } else {
            usernameLabel.setText("-");
        }

        AppState state = JsonStateService.getInstance().load();

        // Gebäude
        int buildingCount = getBuildingCount();
        buildingsCount.setText(String.valueOf(buildingCount));
        updateAddBuildingVisibility(buildingCount);

        // Räume (nur fürs Haus des Users)
        String houseId = (u != null) ? u.getHouseId() : null;
        int roomCount = (houseId == null || houseId.isBlank()) ? 0 : state.getRoomsByHouseId(houseId).size();
        roomsCount.setText(String.valueOf(roomCount));

        // Geräte / Sensoren
        int sensors = state.getAllSensors().size();
        int actors = state.getAllActors().size();
        devicesCount.setText(String.valueOf(sensors + actors));
        sensorsCount.setText(String.valueOf(sensors));

        // Regeln (noch nicht implementiert)
        rulesCount.setText("0");
    }

    private int getBuildingCount() {
        User user = CurrentUser.getCurrentUser();
        if (user == null) return 0;

        String hid = user.getHouseId();
        if (hid == null || hid.isBlank()) return 0;
        return (user.getHouseId() == null || user.getHouseId().isBlank()) ? 0 : 1;
    }

    private void updateAddBuildingVisibility(int buildingCount) {
        boolean showAdd = (buildingCount == 0);
        if (addBuildingCard != null) {
            addBuildingCard.setVisible(showAdd);
            addBuildingCard.setManaged(showAdd);
        }
    }

    // Wird von onMouseClicked der Gebäude-Kachel aufgerufen
    @FXML
    private void openBuildings() {
        App.setRoot("buildingCockpit");
    }


    @FXML
    private void goDashboard(){
        App.setRoot("dashboard");
    }
    @FXML
    private void goLandingPage() { //testmichi
        App.setRoot("landingPage");
    }


    @FXML
    private void goSettings(){
        App.setRoot("settings");
    }
    @FXML
    private void logout(){
        App.setRoot("login");
    }

    @FXML
    private void goUserCockpit() {
        App.setRoot("userCockpit");  // userCockpit.fxml laden
    }

    @FXML
    private void goCreateBuilding() {
        App.setRoot("buildingCockpit");
    }

    @FXML
    private void goBuildingCockpit() {
        App.setRoot("buildingCockpit");
    }



    @FXML
    private void goCreateRoom() {
        RoomController.editMode = false;// Edit-Modus deaktivieren
        App.setRoot("createRoom");  // createRoom.fxml laden
    }

    @FXML
    private void goDeviceCockpit() {
        DeviceController.viewMode = DeviceController.ViewMode.LIST;
        App.setRoot("deviceCockpit");   // deviceCockpit.fxml
    }

    @FXML
    private void goCreateDevice() {
        DeviceController.viewMode = DeviceController.ViewMode.CREATE;
        App.setRoot("deviceCockpit");
    }
    @FXML
    private void goRoomCockpit() {
        App.setRoot("rooms");   // zuerst Liste, dort Raum auswählen
    }

}
