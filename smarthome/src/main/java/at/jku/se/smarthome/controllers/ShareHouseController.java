package at.jku.se.smarthome.controllers;


import at.jku.se.smarthome.App;
import at.jku.se.smarthome.service.HouseService;
import at.jku.se.smarthome.service.HouseShareService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.regex.Pattern;

public class ShareHouseController {
    @FXML private Hyperlink backLink;
    @FXML private TextField emailField;
    @FXML private Button inviteButton;
    @FXML private ListView<String> accessList;

    private final HouseShareService houseShareService = new HouseShareService();

    private final ObservableList<String> sharedMails = FXCollections.observableArrayList();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");

    @FXML
    public void initialize() {
        accessList.setItems(sharedMails);
        accessList.setPlaceholder(new Label("Noch keine Zugriffe vergeben."));

        emailField.setOnAction(e-> onInvite());

        accessList.setCellFactory(list-> new EmailPillCell(sharedMails));

    }
    @FXML
    private void onInvite() {
            String email = normalizeEmail(emailField.getText());

            if (email.isBlank()) {
                showValidation("Bitte E-Mail eingeben.");
                return;
            }
            if (!isValidEmail(email)) {
                showValidation("Bitte eine gültige E-Mail eingeben.");
                return;
            }

            houseShareService.shareHouseWithUser(email);
            accessList.getItems().setAll(houseShareService.getSharedUserEmails());
            emailField.clear();
    }
    @FXML
    private void onBack() {
        //Back to dashboard
        App.setRoot("buildingCockpit");
    }
    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    private void showValidation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private class EmailPillCell extends ListCell<String> {
        private final ObservableList<String> backingList;

        private final HBox root = new HBox(10);
        private final Label emailLabel = new Label();
        private final Button removeButton = new Button("X");

        EmailPillCell(ObservableList<String> backingList) {
            this.backingList = backingList;

            root.setAlignment(Pos.CENTER_LEFT);
            root.getStyleClass().add("pill");

            emailLabel.getStyleClass().add("pill-text");
            HBox.setHgrow(emailLabel, Priority.ALWAYS);
            emailLabel.setMaxWidth(Double.MAX_VALUE);

            removeButton.getStyleClass().add("pill-remove");
            removeButton.setFocusTraversable(false);
            removeButton.setOnAction(e -> {
                String email = getItem();
                if (email == null) return;
                //1 State updaten
                houseShareService.unshareHouseWithUser(email);
                //2 Liste updaten
                backingList.setAll(houseShareService.getSharedUserEmails());
            });
            root.getChildren().addAll(emailLabel, removeButton);

        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                emailLabel.setText(item);
                setText(null);
                setGraphic(root);
            }
        }
    }
}
