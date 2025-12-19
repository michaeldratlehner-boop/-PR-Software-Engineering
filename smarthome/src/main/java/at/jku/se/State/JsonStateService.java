package at.jku.se.State;

import at.jku.se.smarthome.model.devices.SmartDevice;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonStateService {
    private static JsonStateService instance;
    private final Path path = Paths.get("database.json");
    private final Gson gson;

    private JsonStateService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithModifiers(java.lang.reflect.Modifier.STATIC)
                .registerTypeAdapter(SmartDevice.class, new SmartDeviceTypeAdapter())
                .create();
    }

    public static JsonStateService getInstance() {
        if (instance == null) {
            instance = new JsonStateService();
        }
        return instance;
    }

    private void createIfNotExist() {
        try {
            if (!Files.exists(path)) {
                Files.writeString(path, "{}");
                System.out.println("Datenbank-Datei wurde neu erstellt.");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Konnte die Datenbank-Datei nicht erstellen!", ex);
        }
    }

    public AppState load() {
        try {
            createIfNotExist();
            String json = Files.readString(path);

            if (json.trim().equals("{}") || json.trim().isEmpty()) {
                return AppState.getInstance();
            }

            AppState loadedState = gson.fromJson(json, AppState.class);

            if (loadedState == null) {
                return AppState.getInstance();
            }

            AppState.setInstance(loadedState);

            return loadedState;
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Datenbank. Erstelle neue leere Datenbank.");
            e.printStackTrace();

            try {
                Files.deleteIfExists(path);
                createIfNotExist();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return AppState.getInstance();
        }
    }

    public void save(AppState state) {
        try {
            String json = gson.toJson(state);
            Files.writeString(path, json);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Speichern der Datenbank!", e);
        }
    }


     /* TypeAdapter für SmartDevice */
    private static class SmartDeviceTypeAdapter implements JsonSerializer<SmartDevice>,
            JsonDeserializer<SmartDevice> {
        private static final String CLASS_NAME_FIELD = "className";

        @Override
        public JsonElement serialize(SmartDevice src, java.lang.reflect.Type typeOfSrc,
                                     JsonSerializationContext context) {
            // Speichere den vollständigen Klassennamen
            JsonObject result = context.serialize(src, src.getClass()).getAsJsonObject();
            result.addProperty(CLASS_NAME_FIELD, src.getClass().getName());
            return result;
        }

        @Override
        public SmartDevice deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                                       JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            if (!jsonObject.has(CLASS_NAME_FIELD)) {
                throw new JsonParseException("Fehlender Klassenname für SmartDevice");
            }

            String className = jsonObject.get(CLASS_NAME_FIELD).getAsString();

            try {
                // Lade die Klasse dynamisch
                Class<?> clazz = Class.forName(className);
                return context.deserialize(json, clazz);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException("Unbekannte SmartDevice-Klasse: " + className, e);
            } catch (Exception e) {
                throw new JsonParseException("Fehler beim Deserialisieren von SmartDevice: " + className, e);
            }
        }
    }
}
