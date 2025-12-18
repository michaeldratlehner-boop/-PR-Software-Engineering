package at.jku.se.smarthome.controllers;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentHouse;
import at.jku.se.State.CurrentUser;
import at.jku.se.State.JsonStateService;
import at.jku.se.smarthome.App;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.service.HouseService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.List;


public class BuildingController {

    private final HouseService houseService = new HouseService();

    public enum ViewMode {
        LIST,    // Liste der Gebäude
        CREATE,  // neues Gebäude anlegen
        EDIT     // bestehendes Gebäude bearbeiten (später)
    }

    // wird vor App.setRoot("building") von außen gesetzt
    public static ViewMode viewMode = ViewMode.LIST;

    // Popup-Overlay
    @FXML private VBox deletePopup;
    @FXML private Label deleteBuildingNameLabel;
    // die aktuell ausgewählte Zeile in der Liste
    private HBox rowToDelete;

    // --- List-View ---
    @FXML private VBox listCard;
    @FXML private VBox buildingsListBox;
    @FXML private Label exampleBuildingLabel;

    // --- Form-View ---
    @FXML private VBox formCard;
    @FXML private Label formTitleLabel;
    @FXML private Label formSubtitleLabel;

    @FXML private TextField nameField;
    @FXML private TextField floorsField;
    @FXML private TextField addressField;

    @FXML private Label userIdLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;

    @FXML
    public void initialize() {
        // Dummy-Daten für Smart Home Informationen
        if (userIdLabel != null) {
            userIdLabel.setText(CurrentUser.getCurrentUser().getId());
        }
        if (latitudeLabel != null) {
            latitudeLabel.setText("-" + "°N");
        }
        if (longitudeLabel != null) {
            longitudeLabel.setText("-" + "°E");
        }

        if (floorsField != null &&
                (floorsField.getText() == null || floorsField.getText().isBlank())) {
            floorsField.setText("1");
        }

        if (exampleBuildingLabel != null) {
            exampleBuildingLabel.setText("Meine Wohnung");
        }

        updateView();
    }

    // -------- View-Umschalten --------

    private void updateView() {
        if (listCard == null || formCard == null) return;

        switch (viewMode) {
            case LIST -> showList();
            case CREATE -> showForm(false);
            case EDIT -> showForm(true);
        }
    }

    private void showList() {
        listCard.setVisible(true);
        listCard.setManaged(true);

        formCard.setVisible(false);
        formCard.setManaged(false);

        buildingsListBox.getChildren().clear();

        AppState state = JsonStateService.getInstance().load();
        User user = CurrentUser.getCurrentUser();

        List<House> houses = state.getAllHousesForUser(user);

        if(houses.isEmpty()) {
            Label empty = new Label("Keine Gebäude vorhanden.");
            buildingsListBox.getChildren().add(empty);
            return;
        }

        for (House house : houses) {
            buildingsListBox.getChildren().add(createHouseRow(house));
        }


    }

    private HBox createHouseRow(House house) {
        Label nameLabel = new Label(house.getName());
        Button edit = new Button("Bearbeiten");
        Button delete = new Button("Löschen");
        Button share = new Button("Freigeben");

        edit.setOnAction(e -> {
            CurrentHouse.setCurrentHouse(house);
            viewMode = ViewMode.EDIT;
            updateView();
        });
        delete.setOnAction(e -> {
            CurrentHouse.setCurrentHouse(house);
            deleteBuilding(e);
        });

        share.setOnAction(e -> {
            App.setRoot("shareHouse");
            System.out.println("Gebäude freigeben: " + house.getName());
        });

        HBox row = new HBox(10, nameLabel, edit, share, delete);
        return row;

    }

    private void showForm(boolean edit) {
        formCard.setVisible(true);
        formCard.setManaged(true);

        listCard.setVisible(false);
        listCard.setManaged(false);

        if (edit) {
            formTitleLabel.setText("Gebäude bearbeiten");
            formSubtitleLabel.setText("Bearbeiten Sie die Daten Ihres Gebäudes");
            House current = CurrentHouse.getCurrentHouse();
            if (current != null) {
                latitudeLabel.setText(current.getLatitude() + "°N");
                longitudeLabel.setText(current.getLongitude() + "°E");
                // Formular mit Daten des aktuellen Gebäudes füllen
                nameField.setText(current.getName());
                floorsField.setText(String.valueOf(current.getFloors()));
                addressField.setText(current.getAddress());
            }
        } else {
            formTitleLabel.setText("Gebäude erstellen");
            formSubtitleLabel.setText("Fügen Sie Ihr Haus oder Wohnung hinzu");
            clearForm();
        }
    }

    private void clearForm() {
        if (nameField != null) nameField.clear();
        if (floorsField != null) floorsField.setText("1");
        if (addressField != null) addressField.clear();
    }

    // -------- Buttons oben/unten --------

    @FXML
    private void backToDashboard() {
        App.setRoot("dashboard");
    }

    @FXML
    private void saveBuilding() {
        String name = nameField.getText();
        String floors = floorsField.getText();
        String address = addressField.getText();
        try{
        if (viewMode == ViewMode.EDIT) {
            House current = CurrentHouse.getCurrentHouse();
            if(current == null){
                throw new IllegalStateException("Kein Gebäude zum Bearbeiten ausgewählt.");
            }
            House updatedHouse = houseService.updateHouse(
                    current.getId(),
                    nameField.getText(),
                    Integer.parseInt(floorsField.getText()),
                    addressField.getText()
            );
            CurrentHouse.setCurrentHouse(updatedHouse);
            System.out.println("Gebäude aktualisieren: "+updatedHouse.getId());
        } else {
            House createdHouse = houseService.createHouse(name, Integer.parseInt(floors), address);
            CurrentHouse.setCurrentHouse(createdHouse);
            System.out.println("Gebäude mit Id gespeichert: "+createdHouse.getId());
        }

//        System.out.println("Name: " + name);
//        System.out.println("Stockwerke: " + floors);
//        System.out.println("Adresse: " + address);

        // Verhalten wie bisher:
        // nach Speichern zurück zum Dashboard
        App.setRoot("dashboard");
        }    catch(Exception e){
        System.out.println("Fehler beim Speichern des Gebäudes: "+e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        // Abbrechen → wie bisher zurück zum Dashboard
        App.setRoot("dashboard");
    }

    @FXML
    private void increaseFloors() {
        int current = parseFloors();
        floorsField.setText(String.valueOf(current + 1));
    }

    @FXML
    private void decreaseFloors() {
        int current = parseFloors();
        if (current > 1) {
            floorsField.setText(String.valueOf(current - 1));
        }
    }

    private int parseFloors() {
        try {
            return Integer.parseInt(floorsField.getText().trim());
        } catch (Exception e) {
            return 1;
        }
    }

    // -------- Aktionen in der Liste --------

    @FXML
    private void editSelectedBuilding() {
        System.out.println("Gebäude bearbeiten (Dummy)...");
        viewMode = ViewMode.EDIT;
        showForm(true);
    }

    @FXML
    private void shareSelectedBuilding() {
        System.out.println("Gebäude freigeben (Dummy)...");
    }

    @FXML
    private void deleteBuilding(ActionEvent event) {
        System.out.println("Gebäude löschen (Popup öffnen)...");

        // Zeile (HBox) ermitteln, aus der der Button geklickt wurde
        Button sourceButton = (Button) event.getSource();
        rowToDelete = (HBox) sourceButton.getParent();

        // Gebäudename: erstes Kind in der HBox ist Label
        Label nameLabel = (Label) rowToDelete.getChildren().get(0);
        String buildingName = nameLabel.getText();

        if (deleteBuildingNameLabel != null) {
            deleteBuildingNameLabel.setText(buildingName);
        }

        // Popup anzeigen
        deletePopup.setVisible(true);
        deletePopup.setManaged(true);
    }

    @FXML
    private void cancelDeleteBuilding() {
        // Popup schließen, nichts löschen
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }

    @FXML
    private void confirmDeleteBuilding() {
        if (rowToDelete != null) {
            // Aus der UI entfernen
            buildingsListBox.getChildren().remove(rowToDelete);

            // TODO: hier auch  json löschen
            System.out.println("Gebäude wirklich gelöscht.");
        }
        deletePopup.setVisible(false);
        deletePopup.setManaged(false);
        rowToDelete = null;
    }


}

