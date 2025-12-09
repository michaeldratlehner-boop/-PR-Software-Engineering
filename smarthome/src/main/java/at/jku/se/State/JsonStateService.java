package at.jku.se.State;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonStateService {
    private static JsonStateService instance;
    private final Path path = Paths.get("database.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JsonStateService() {}

    public static JsonStateService getInstance() {
        if (instance == null) {
            instance = new JsonStateService();
        }
        return instance;
    }

    private void createIfNotExist(){
        try {
            Files.readString(path);
        }catch (IOException e) {
            try {
                Files.writeString(path, "{}");
                System.out.println("Datenbank-Datei wurde neu erstellt.");
            } catch (IOException ex) {
                throw new RuntimeException("Konnte die Datenbank-Datei nicht erstellen!", ex);
            }
        }
    }
    public AppState load() {
        try{
            createIfNotExist();

            String json = Files.readString(path);
            AppState state = gson.fromJson(json, AppState.class);

            if(state == null){
                state = new AppState();
            }
            return state;
        }catch (Exception e) {
            throw new RuntimeException("Fehler beim Laden der Datenbank!", e) ;
        }
    }
    public void save(AppState state) {
        try{
            String json = gson.toJson(state);
            Files.writeString(path, json);
        }catch (Exception e) {
            throw new RuntimeException("Fehler beim Speichern der Datenbank!", e) ;
        }
    }


}
