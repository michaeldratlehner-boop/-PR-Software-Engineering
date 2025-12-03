package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void login() {
        App.setRoot("dashboard");
    }

    @FXML
    private void goToRegister() {
        App.setRoot("register");
    }

    @FXML
    private void backToStart() {

    }
}