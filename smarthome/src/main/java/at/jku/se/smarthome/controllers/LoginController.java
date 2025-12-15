package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import at.jku.se.smarthome.service.UserService;
import at.jku.se.smarthome.model.User;
import at.jku.se.State.CurrentUser;

public class LoginController {

    private final UserService userService = new UserService();

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void login() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        // 1. Pflichtfelder prüfen
        if (email.isEmpty()) {
            errorLabel.setText("Bitte E-Mail-Adresse eingeben.");
            return;
        }

        /*if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            errorLabel.setText("Bitte eine gültige E-Mail-Adresse eingeben.");
            return;
        }*/ //zum testen auskommentiert!!!!

        if (password.isEmpty()) {
            errorLabel.setText("Bitte Passwort eingeben.");
            return;
        }


        try{
            // 2. Benutzer authentifizieren und setzen
            User user = userService.loginUser(email, password);


            //3. Erfolgreich
            errorLabel.setText("");
            App.setRoot("dashboard");

        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            return;
        }


    }

    @FXML
    private void goToRegister() {
        errorLabel.setText("");
        App.setRoot("register");
    }

    @FXML
    private void goToDashboard() {
        errorLabel.setText("");
        App.setRoot("dashboard");
    }
    @FXML
    private void backToLandingPage() {   // „Zurück zur Startseite“-Button
        errorLabel.setText("");
        App.setRoot("landingPage");
    }
}
