package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserProfileEditController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Label errorLabel;
    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;

    @FXML
    public void initialize() {
        // Dummy-Daten (später aus User-Objekt/DB laden)
        firstNameField.setText("Max");
        lastNameField.setText("Mustermann");
        emailField.setText("max.beispiel@example.com");
        addressField.setText("Musterstraße 1, 1010 Wien");

        userIdLabel.setText("0e6422be-81cc-41b3-9e46-661b3e3bc679");
        latitudeLabel.setText("48.2082°N");
        longitudeLabel.setText("16.3738°E");
    }

    @FXML
    private void saveProfile() {
        String first = firstNameField.getText().trim();
        String last  = lastNameField.getText().trim();
        String addr  = addressField.getText().trim();
        String pwd   = passwordField.getText();
        String pwd2  = confirmPasswordField.getText();

        if (first.isEmpty() || last.isEmpty() || addr.isEmpty()) {
            errorLabel.setText("Bitte alle Pflichtfelder (*) ausfüllen.");
            return;
        }
        if (!pwd.isEmpty() || !pwd2.isEmpty()) {
            if (!pwd.equals(pwd2)) {
                errorLabel.setText("Passwörter stimmen nicht überein.");
                return;
            }
            if (pwd.length() < 6) {
                errorLabel.setText("Passwort muss mindestens 6 Zeichen haben.");
                return;
            }
        }

        // hier später: Änderungen speichern (Service/Repository)
        System.out.println("Profil gespeichert: " + first + " " + last + ", " + addr);

        errorLabel.setText("");
        App.setRoot("profile");   // zurück ins Usercockpit
    }

    @FXML
    private void cancelEdit() {
        errorLabel.setText("");
        App.setRoot("profile");
    }

    @FXML
    private void backToDashboard() {
        errorLabel.setText("");
        App.setRoot("dashboard");
    }
}
