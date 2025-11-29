package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;

public class DashboardController {
    @FXML
    private void goDashboard(){
        App.setRoot("dashboard");
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
