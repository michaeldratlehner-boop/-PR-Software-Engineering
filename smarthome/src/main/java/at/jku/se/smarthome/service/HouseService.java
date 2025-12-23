package at.jku.se.smarthome.service;

import at.jku.se.State.*;
import at.jku.se.query.AppStateMutations;
import at.jku.se.query.AppStateQuery;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.User;

import java.util.ArrayList;
import java.util.List;

public class HouseService { ;
    private final AppState appState = AppState.getInstance();
    private final AppStateQuery appStateQuery = new AppStateQuery(appState);
    private final AppStateMutations appStateMutations = new AppStateMutations(appState);
    public House createHouse(String name, int floors, String address) {
        House newHouse = new House(name, floors, address);

        GeocodingService.Coordinates coords = new GeocodingService().geocodeAddress(address);
        if(coords != null) {
            newHouse.setLatitude(coords.lat);
            newHouse.setLongitude(coords.lon);
        }

        appStateMutations.saveHouse(newHouse);

        //House mit User verknüpfen über die ID
        User currentUser = CurrentUser.getCurrentUser();
        currentUser.setHouseId(newHouse.getId());
        appStateMutations.saveUser(currentUser);

        return newHouse;
    }

    public House updateHouse(String houseId, String name, int floors, String address) {
        House house = appStateQuery.getHouse(houseId);
        if(house == null) {
            throw new IllegalArgumentException("Haus nicht gefunden");
        }

        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Hausname darf nicht leer sein");
        }
        if(floors <= 0) {
            throw new IllegalArgumentException("Anzahl der Stockwerke muss größer als 0 sein");
        }

        if(address == null || address.isBlank()) {
            throw new IllegalArgumentException("Adresse darf nicht leer sein");
        }


        boolean addressChanged = !address.equals(house.getAddress());

        house.setName(name);
        house.setFloors(floors);
        house.setAddress(address);

        if(addressChanged) {
            GeocodingService geocoder = new GeocodingService();
            GeocodingService.Coordinates coords = geocoder.geocodeAddress(address);
            if (coords != null) {
                house.setLatitude(coords.lat);
                house.setLongitude(coords.lon);
            }
        }

        appStateMutations.saveHouse(house);

        return house;
    }
    public void deleteHouse(String houseId) {
        appStateMutations.deleteHouse(houseId);
    }

}
