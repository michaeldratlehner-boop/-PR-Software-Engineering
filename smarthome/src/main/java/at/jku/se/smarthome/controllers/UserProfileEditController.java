package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import at.jku.se.State.CurrentUser;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.service.UserService;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.nio.file.Paths;



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
    @FXML private ImageView avatarImageView;
    private File selectedAvatarFile;


    @FXML
    public void initialize() {
        User u = CurrentUser.getCurrentUser();
        if (u == null) return;

        firstNameField.setText(u.getFirstName());
        lastNameField.setText(u.getLastName());
        emailField.setText(u.getEmail());
        addressField.setText(u.getAddress());

        userIdLabel.setText(u.getId());

        Double lat = u.getLatitude();
        Double lon = u.getLongitude();
        latitudeLabel.setText(lat == null ? "-" : lat + "°N");
        longitudeLabel.setText(lon == null ? "-" : lon + "°E");

        // Initialen immer setzen
        String initials = (u.getFirstName().substring(0,1) + u.getLastName().substring(0,1)).toUpperCase();
        avatarInitialsLabel.setText(initials);
        avatarInitialsLabel.setVisible(true);

        // Avatar laden
        String p = u.getAvatarPath();
        System.out.println("profileEdit avatarPath=" + p);

        if (p != null && !p.isBlank()) {
            File f = Paths.get(p).toFile();
            System.out.println("profileEdit absolute=" + f.getAbsolutePath());
            System.out.println("profileEdit exists=" + f.exists());

            if (f.exists()) {
                avatarImageView.setImage(new Image(f.toURI().toString(), false));
                avatarInitialsLabel.setVisible(false);
            }
        }


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

            User updatedUser = userService.updateUserProfile(current.getId(), first, last, passwordForUpdate, addr, selectedAvatarFile);

            CurrentUser.setCurrentUser(updatedUser);

            errorLabel.setText("");
            App.setRoot("userCockpit");   // zurück ins Usercockpit

        }catch (IllegalArgumentException e){
            errorLabel.setText(e.getMessage());
            return;
        }

    }

    @FXML
    private void uploadAvatar() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Bild auswählen");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fc.showOpenDialog(firstNameField.getScene().getWindow());
        if (file == null) return;

        selectedAvatarFile = file;

        if (avatarImageView != null) {
            avatarImageView.setImage(new Image(file.toURI().toString(), false));
        }
        if (avatarInitialsLabel != null) {
            avatarInitialsLabel.setVisible(false);
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
