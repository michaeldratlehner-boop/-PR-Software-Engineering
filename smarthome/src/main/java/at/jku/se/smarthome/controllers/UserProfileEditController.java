package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import at.jku.se.State.CurrentUser;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.service.UserService;

public class UserProfileEditController {

    private final UserService userService = new UserService();

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label avatarInitialsLabel;
    @FXML private Label errorLabel;
    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;

    @FXML
    public void initialize() {
        // Dummy-Daten (später aus User-Objekt/DB laden)
        firstNameField.setText(CurrentUser.getCurrentUser().getFirstName());
        lastNameField.setText(CurrentUser.getCurrentUser().getLastName());
        emailField.setText(CurrentUser.getCurrentUser().getEmail());
        addressField.setText(CurrentUser.getCurrentUser().getAddress());

        userIdLabel.setText(CurrentUser.getCurrentUser().getId());
        latitudeLabel.setText(CurrentUser.getCurrentUser().getLatitude() + "°N");
        longitudeLabel.setText(CurrentUser.getCurrentUser().getLongitude() + "°E");

        // Initialen für Avatar
        String initials = (CurrentUser.getCurrentUser().getFirstName().substring(0,1) + CurrentUser.getCurrentUser().getLastName().substring(0,1)).toUpperCase();
        avatarInitialsLabel.setText(initials);
    }

    @FXML
    private void saveProfile() {
        User current = CurrentUser.getCurrentUser();
        if(current == null) {
            errorLabel.setText("Kein Benutzer angemeldet.");
            return;
        }

        String first = firstNameField.getText().trim();
        String last  = lastNameField.getText().trim();
        String addr  = addressField.getText().trim();
        String pwd   = passwordField.getText();
        String pwd2  = confirmPasswordField.getText();

        if (first.isEmpty() || last.isEmpty() || addr.isEmpty()) {
            errorLabel.setText("Bitte alle Pflichtfelder (*) ausfüllen.");
            return;
        }
        boolean changePassword = !pwd.isEmpty() || !pwd2.isEmpty();

        if (changePassword) {
            if (!pwd.equals(pwd2)) {
                errorLabel.setText("Passwörter stimmen nicht überein.");
                return;
            }
            if (pwd.length() < 8) {
                errorLabel.setText("Passwort muss mindestens 8 Zeichen haben.");
                return;
            }
        }
        try{
            String passwordForUpdate = changePassword ? pwd : null;

            User updatedUser = userService.updateUserProfile(
                current.getId(), first, last, passwordForUpdate, addr
            );
            CurrentUser.setCurrentUser(updatedUser);

            errorLabel.setText("");
            App.setRoot("userCockpit");   // zurück ins Usercockpit

        }catch (IllegalArgumentException e){
            errorLabel.setText(e.getMessage());
            return;
        }

    }

    @FXML
    private void cancelEdit() {
        errorLabel.setText("");
        App.setRoot("userCockpit");
    }

    @FXML
    private void backToDashboard() {
        errorLabel.setText("");
        App.setRoot("dashboard");
    }
}
