package at.jku.se.smarthome.controllers;


import at.jku.se.smarthome.App;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.UUID;

public class AutomationAddController {
    @FXML
    private TextField automationName;
    @FXML
    private ToggleButton enabledToggle;
    @FXML
    private ComboBox<String> sensorCombo;
    @FXML
    private ComboBox<String> operatorCombo;
    @FXML
    private TextField valueField;
    @FXML
    private ComboBox<String> unitCombo;
    @FXML
    private HBox valueChoiceRow;
    @FXML
    private ComboBox<String> valueChoiceCombo;
    @FXML
    private ComboBox<String> deviceCombo;
    @FXML
    private ComboBox<String> actionCombo;
    @FXML
    private Text hintText;

    @FXML
    public void initialize() {
        enabledToggle.setSelected(true);
        hintText.setText("");

        //Sensor Dummy Data
        sensorCombo.setItems(FXCollections.observableArrayList("Temparatursensor", "Lichtsensor", "Bewegungsmelder", "Rauchmelder", "Fenstersensor", "Türsensor"));
        sensorCombo.getSelectionModel().selectFirst();

        //Operator Dummy Data
        operatorCombo.setItems(FXCollections.observableArrayList("==", "!=", ">", "<"));
        operatorCombo.getSelectionModel().selectFirst();

        //Einheiten
        unitCombo.setItems(FXCollections.observableArrayList("°C", "Lux", "%"));
        unitCombo.getSelectionModel().selectFirst();

        //Devices Dummy Data
        deviceCombo.setItems(FXCollections.observableArrayList("Heizungssteuerung", "Licht", "Sicherheitssystem", "Wohnungstüre", "Fenster"));
        deviceCombo.getSelectionModel().selectFirst();

        //Actions Dummy Data
        actionCombo.setItems(FXCollections.observableArrayList("Einschalten", "Ausschalten", "Alarm auslösen", "Tür öffnen", "Fenster schließen", "Tür schließen", "Fenster öffnen", "Sperren", "Entsperren"));
        actionCombo.getSelectionModel().selectFirst();

        //Dynamik je nach Sensor
        sensorCombo.valueProperty().addListener((observable, oldValue, newValue) -> adaptUiToSensor(newValue));
        adaptUiToSensor(sensorCombo.getValue());
    }

    private void adaptUiToSensor(String sensor) {
        if (sensor == null) {
            return;
        }

        //Default nummerischer Wert
        showNumericInput();

        if (sensor.equals("Temparatursensor")) {
            operatorCombo.setItems(FXCollections.observableArrayList("<", ">", "=="));
            unitCombo.setItems(FXCollections.observableArrayList("°C"));
            unitCombo.getSelectionModel().selectFirst();
            valueField.setPromptText("z.B. 20");
        } else if (sensor.equals("Lichtsensor")) {
            operatorCombo.setItems(FXCollections.observableArrayList("<", ">", "=="));
            unitCombo.setItems(FXCollections.observableArrayList("Lux"));
            unitCombo.getSelectionModel().selectFirst();
            valueField.setPromptText("z.B. 300");
        } else if (sensor.equals("Bewegungsmelder") || sensor.equals("Rauchmelder") || sensor.equals("Fenstersensor")) {
            operatorCombo.setItems(FXCollections.observableArrayList("=="));
            unitCombo.setVisible(false);
            unitCombo.setManaged(false);
            showChoiceInput(List.of("True", "False"));
        } else if (sensor.equals("Türsensor")) {
            operatorCombo.setItems(FXCollections.observableArrayList("=="));
            unitCombo.setVisible(false);
            unitCombo.setManaged(false);
            showChoiceInput(List.of("OFFEN", "GESCHLOSSEN", "GESPERRT"));
        }
    }
    private void showNumericInput() {
        valueField.setVisible(true);
        valueField.setManaged(true);

        valueChoiceRow.setVisible(false);
        valueChoiceRow.setManaged(false);

        unitCombo.setVisible(true);
        unitCombo.setManaged(true);
    }
    private void showChoiceInput(List<String> choices) {
        valueField.setVisible(false);
        valueField.setManaged(false);

        valueChoiceRow.setVisible(true);
        valueChoiceRow.setManaged(true);

        valueChoiceCombo.setItems(FXCollections.observableArrayList(choices));
        valueChoiceCombo.getSelectionModel().selectFirst();
    }
    @FXML
    private void onSave(){
        hintText.setText("");
        if(automationName.getText().trim().isEmpty()){
            hintText.setText("Bitte einen Namen eingeben");
            return;
        }

        String value = valueField.isManaged()
                ? valueField.getText().trim()
                : valueChoiceCombo.getValue();

        if (value == null || value.isEmpty()) {
            hintText.setText("Bitte einen Wert angeben.");
            return;
        }

        // Dummy-Ausgabe (Simulation eines Speicherns)
        String debug = """
                Regel erstellt:
                Name: %s
                Aktiviert: %s
                WENN: %s %s %s %s
                DANN: %s → %s
                """.formatted(
                automationName.getText(),
                enabledToggle.isSelected(),
                sensorCombo.getValue(),
                operatorCombo.getValue(),
                value,
                unitCombo.isManaged() ? unitCombo.getValue() : "",
                deviceCombo.getValue(),
                actionCombo.getValue()
        );

        System.out.println(debug);
        hintText.setText("Regel erstellt ✅");
        automationName.clear();
        valueField.clear();
    }

    @FXML
    private void onCancel() {
        hintText.setText("Abgebrochen.");
    }

    @FXML
    private void onBack() {
        App.setRoot("dashboard");
        hintText.setText("Zurück (TODO Navigation)");
    }
}
