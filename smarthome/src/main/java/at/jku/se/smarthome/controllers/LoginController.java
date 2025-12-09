package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

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

        // 2. Dummy-Loginprüfung (später durch DB/Service ersetzen)
       /* if (!(email.equals("max@beispiel.at") && password.equals("123456"))) {
            errorLabel.setText("E-Mail oder Passwort ist falsch.");
            return;
        }*/

        // 3. Erfolgreich
        errorLabel.setText("");
        App.setRoot("dashboard");
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
