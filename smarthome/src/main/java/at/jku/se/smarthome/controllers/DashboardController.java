package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label usernameLabel;
    @FXML private Label roomsCount;
    @FXML private Label devicesCount;
    @FXML private Label rulesCount;
    @FXML private Label sensorsCount;

    @FXML
    public void initialize(){
        usernameLabel.setText("Michael Drahtlehner");
        roomsCount.setText("0");
        devicesCount.setText("0");
        rulesCount.setText("0");
        sensorsCount.setText("0");
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
}
