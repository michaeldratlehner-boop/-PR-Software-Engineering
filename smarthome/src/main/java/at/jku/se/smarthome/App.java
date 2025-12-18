package at.jku.se.smarthome;

import at.jku.se.smarthome.model.User;
import at.jku.se.State.CurrentUser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage primaryStage;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

//        //fake Login für die Entwicklung
//        User demo = new User();
//        demo.setFirstName("Michael");
//        demo.setLastName("Drahtlehner");
//        CurrentUser.setCurrentUser(demo);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/landingPage.fxml"));
        scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        stage.setTitle("Smarthome Simulator");
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
            scene.setRoot(loader.load());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
