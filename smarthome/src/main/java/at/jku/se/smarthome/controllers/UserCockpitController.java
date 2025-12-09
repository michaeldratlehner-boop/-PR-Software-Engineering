package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserCockpitController {

    @FXML private Label avatarInitialsLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Label locationLabel;

    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;
    @FXML private Label infoTextLabel;

    @FXML
    public void initialize() {
        // Dummy-Daten
        String firstName = "Max";
        String lastName  = "Mustermann";

        nameLabel.setText(firstName + " " + lastName);
        emailLabel.setText("max.mustermann@example.com");
        addressLabel.setText("Musterstraße 1, 1010 Wien");
        locationLabel.setText("48.2082°N, 16.3738°E");

        userIdLabel.setText("0e642b2e-81cc-41b3-9e46-661b3e3bc679");
        latitudeLabel.setText("48.2082°N");
        longitudeLabel.setText("16.3738°E");

        // Initialen für Avatar
        String initials = (firstName.substring(0,1) + lastName.substring(0,1)).toUpperCase();
        avatarInitialsLabel.setText(initials);
    }

    @FXML
    private void backToDashboard() {
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

}
