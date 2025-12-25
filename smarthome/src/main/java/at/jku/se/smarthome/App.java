package at.jku.se.smarthome;

import at.jku.se.State.JsonStateService;
import at.jku.se.smarthome.model.User;
import at.jku.se.State.CurrentUser;

import at.jku.se.smarthome.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        try {
            JsonStateService.getInstance().load();// Test-Login: existierenden User aus JSON authentifizieren
            new UserService().loginUser("test@gmx.at", "12345678");


            setRoot("dashboard"); // oder z.B. "buildingCockpit"

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback, falls Login fehlschlägt
            setRoot("dashboard");
        }

        stage.setTitle("Smarthome Simulator");
        stage.show();

    }

    public static void setRoot(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
