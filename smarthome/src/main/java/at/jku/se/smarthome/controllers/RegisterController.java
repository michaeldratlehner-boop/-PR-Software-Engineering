package at.jku.se.smarthome.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void register() {
        System.out.println("Registriere Benutzer...");
    }

    @FXML
    private void backToLogin() {
        System.out.println("Zurück zum Login...");
    }
}