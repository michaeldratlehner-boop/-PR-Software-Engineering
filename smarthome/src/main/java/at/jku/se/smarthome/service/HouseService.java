package at.jku.se.smarthome.service;

import at.jku.se.State.*;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.User;

public class HouseService {
    private final JsonStateService jsonStateService = JsonStateService.getInstance();

    private AppState loadState() {
        return jsonStateService.load();
    }
    public House createHouse(String name, int floors, String address) {
        AppState state = loadState();

        House newHouse = new House(name, floors, address);

        newHouse.setId(state.nextHouseId());

        GeocodingService.Coordinates coords = new GeocodingService().geocodeAddress(address);
        if(coords != null) {
            newHouse.setLatitude(coords.lat);
            newHouse.setLongitude(coords.lon);
        }

        state.saveHouse(newHouse);

        //House mit User verknüpfen über die ID
        User currentUser = CurrentUser.getCurrentUser();
        currentUser.setHouseId(newHouse.getId());
        state.saveUser(currentUser);

        return newHouse;
    }

    public House updateHouse(String houseId, String name, int floors, String address) {
        AppState state = loadState();
        House house = state.getHouse(houseId);
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

        state.saveHouse(house);

        return house;
    }


}
