package at.jku.se.smarthome.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;




public class GeocodingService {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public static class Coordinates {
        public final double lat;
        public final double lon;

        public Coordinates(double latitude, double longitude) {
            this.lat = latitude;
            this.lon = longitude;
        }
    }
    public Coordinates geocodeAddress(String address) {
        try{
            String enccoded = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String Url = "https://nominatim.openstreetmap.org/search?q=" + enccoded + "&format=json&limit=1";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Url))
                .header("User-Agent", "Uni-SmartHomeApp-StudentProject/1.0")
                .GET()
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            JsonArray jsonArray = new JsonParser().parseString(body).getAsJsonArray();
            if(jsonArray.isEmpty()){
                return null;
            }
            JsonElement firstResult = jsonArray.get(0);
            double lat = firstResult.getAsJsonObject().get("lat").getAsDouble();
            double lon = firstResult.getAsJsonObject().get("lon").getAsDouble();

            return new Coordinates(lat, lon);
        }catch(IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
