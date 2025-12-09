package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;

public class LandingPageController {

    @FXML
    private void goToRegister() {
        App.setRoot("register");
    }

    @FXML
    private void goToLogin() {
        App.setRoot("login");
    }

}
