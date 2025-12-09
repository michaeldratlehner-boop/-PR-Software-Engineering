package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.regex.Pattern;
public class RegisterController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;   // aus FXML

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @FXML
    private void register() {
        // Eingaben holen & trimmen
        String firstName = firstNameField.getText().trim();
        String lastName  = lastNameField.getText().trim();
        String email     = emailField.getText().trim();
        String address   = addressField.getText().trim();
        String password  = passwordField.getText();
        String confirm   = confirmPasswordField.getText();

        StringBuilder errors = new StringBuilder();

        // 1. Pflichtfelder
        if (firstName.isEmpty()) {
            errors.append("Vorname darf nicht leer sein.\n");
        }
        if (lastName.isEmpty()) {
            errors.append("Nachname darf nicht leer sein.\n");
        }
        if (email.isEmpty()) {
            errors.append("E-Mail-Adresse darf nicht leer sein.\n");
        }
        if (address.isEmpty()) {
            errors.append("Adresse darf nicht leer sein.\n");
        }
        if (password.isEmpty()) {
            errors.append("Passwort darf nicht leer sein.\n");
        }
        if (confirm.isEmpty()) {
            errors.append("Bitte Passwortbestätigung eingeben.\n");
        }

        // 2. E-Mail-Format (nur wenn überhaupt etwas drin steht)
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("E-Mail-Adresse ist nicht gültig.\n");
        }

        // 3. Passwort-Regeln
        if (!password.isEmpty() && password.length() < 8) {
            errors.append("Passwort muss mindestens 8 Zeichen lang sein.\n");
        }

        // 4. Passwort = Bestätigung?
        if (!password.isEmpty() && !confirm.isEmpty() && !password.equals(confirm)) {
            errors.append("Passwort und Passwortbestätigung stimmen nicht überein.\n");
        }

        // Wenn Fehler vorhanden → anzeigen und abbrechen
        if (errors.length() > 0) {
            errorLabel.setText(errors.toString());
            errorLabel.setVisible(true);
            return;
        }

        // Wenn alles ok → Fehlermeldung ausblenden und weiter
        errorLabel.setVisible(false);

        System.out.println("Registrierung erfolgreich für: " + email);
        App.setRoot("landingPage");
    }

    @FXML
    private void backToLogin() {
        System.out.println("Zurück zum Login...");
        App.setRoot("landingPage");
    }
    @FXML
    private void goToLogin() {        // ← Zu „Bereits registriert? Jetzt anmelden“
        App.setRoot("login");
    }

}