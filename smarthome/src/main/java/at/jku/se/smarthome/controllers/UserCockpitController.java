package at.jku.se.smarthome.controllers;

import at.jku.se.smarthome.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import at.jku.se.State.CurrentUser;

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

}
