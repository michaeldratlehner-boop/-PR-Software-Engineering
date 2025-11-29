package at.jku.se.smarthome.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class ViewLoader {
    public static Pane load(String name) {
        try {
            return FXMLLoader.load(ViewLoader.class.getResource("/fxml/" + name + ".fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Pane();
        }
    }
}
