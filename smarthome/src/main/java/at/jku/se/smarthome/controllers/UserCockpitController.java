package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import at.jku.se.State.CurrentUser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.nio.file.Paths;


public class UserCockpitController {


    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Label locationLabel;

    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;
    @FXML private Label infoTextLabel;
    @FXML private ImageView avatarImageView;
    @FXML private Label avatarInitialsLabel;



    @FXML
    public void initialize() {
        String firstName = CurrentUser.getCurrentUser().getFirstName();
        String lastName  = CurrentUser.getCurrentUser().getLastName();

        nameLabel.setText(firstName + " " + lastName);
        emailLabel.setText(CurrentUser.getCurrentUser().getEmail());
        addressLabel.setText(CurrentUser.getCurrentUser().getAddress());
        locationLabel.setText(CurrentUser.getCurrentUser().getLatitude() + "°N , " + CurrentUser.getCurrentUser().getLongitude() + "°E");

        userIdLabel.setText(CurrentUser.getCurrentUser().getId());
        latitudeLabel.setText(CurrentUser.getCurrentUser().getLatitude() + "°N");
        longitudeLabel.setText(CurrentUser.getCurrentUser().getLongitude() + "°E");

        // Initialen für Avatar
        String initials = (firstName.substring(0,1) + lastName.substring(0,1)).toUpperCase();
        avatarInitialsLabel.setText(initials);
        avatarInitialsLabel.setVisible(true);

        // Avatar laden, wenn vorhanden
        String p = CurrentUser.getCurrentUser().getAvatarPath();
        if (p != null && !p.isBlank()) {
            File f = Paths.get(p).toFile();
            if (f.exists()) {
                avatarImageView.setImage(new Image(f.toURI().toString(), false));
                avatarInitialsLabel.setVisible(false);
            }
        }
    }

    @FXML
    private void backToLandingPage() {
        App.setRoot("dashboard");
    }

    @FXML
    private void logout() {
        // später: Session zurücksetzen o.ä.
        App.setRoot("login");
    }

    @FXML
    private void editProfile() {
        App.setRoot("profilEdit");
    }

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

}
