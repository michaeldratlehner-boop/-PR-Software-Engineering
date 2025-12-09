package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
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
    public void initialize(){
        usernameLabel.setText(CurrentUser.getCurrentUser().getFirstName()+" "+CurrentUser.getCurrentUser().getLastName());
         // Dummy-Werte setzen, später durch echte Logik ersetzen
        roomsCount.setText("0");
        devicesCount.setText("0");
        rulesCount.setText("0");
        sensorsCount.setText("0");
        // Optional: "Gebäude hinzufügen"-Kachel nur zeigen, wenn 0 Gebäude
        updateAddBuildingVisibility();
    }
    private void updateAddBuildingVisibility() {
        int count = Integer.parseInt(buildingsCount.getText());
        boolean showAdd = (count == 0);
        if (addBuildingCard != null) {
            addBuildingCard.setVisible(showAdd);
            addBuildingCard.setManaged(showAdd);
        }
    }

    // Wird von onMouseClicked der Gebäude-Kachel aufgerufen
    @FXML
    private void openBuildings() {
        int count = Integer.parseInt(buildingsCount.getText());

        if (count == 0) {
            // noch keine Gebäude → direkt zur "Gebäude erstellen"-Seite
            App.setRoot("createBuilding");     // createBuilding.fxml
        } else {
            // es gibt schon Gebäude → Liste der angelegten Gebäude anzeigen
            App.setRoot("buildingList");       // buildingList.fxml
        }
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
    private void goRooms(){
        App.setRoot("rooms");
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
        App.setRoot("createBuilding");  // createBuilding.fxml laden
    }

    @FXML
    private void goCreateRoom() {
        App.setRoot("createRoom");  // createRoom.fxml laden
    }

    @FXML
    private void goBuildingCockpit() {
        App.setRoot("buildingCockpit");  // buildingCockpit.fxml laden
    }

    @FXML
    private void goRoomCockpit() {
        App.setRoot("roomCockpit");  // roomCockpit.fxml laden
    }


}
