package at.jku.se.smarthome.service;

import at.jku.se.State.*;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.service.GeocodingService;

public class UserService {
    private final PasswordHasher passwordHasher = new PasswordHasher();
    private final JsonStateService jsonStateService = JsonStateService.getInstance();

    private AppState loadState() {
        return jsonStateService.load();
    }

    public User registerUser(String firstName, String lastName, String email, String password, String address) {
        AppState state = loadState();
        boolean exists = state.getAllUsers().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if(exists) {
            throw new IllegalArgumentException("E-Mail ist bereits registriert");// User with this email already exists
        }
        String hashedPassword = PasswordHasher.hashPassword(password);

        User newUser = new User(firstName, lastName, email, hashedPassword, address);
        GeocodingService.Coordinates coords = new GeocodingService().geocodeAddress(address);
        if(coords != null) {
            newUser.setLatitude(coords.lat);
            newUser.setLongitude(coords.lon);
        }

        state.saveUser(newUser);

        return newUser;
    }

    public User loginUser(String email, String password) {
        AppState state = loadState();

        for(User user : state.getAllUsers()) {
            if(user.getEmail().equalsIgnoreCase(email)) {
                if(PasswordHasher.checkPassword(password, user.getPasswordHash())) {

                    CurrentUser.setCurrentUser(user);
                    if(user.getHouseId() != null) {
                        CurrentHouse.setCurrentHouse(state.getHouse(user.getHouseId()));
                    }else{
                        CurrentHouse.setCurrentHouse(null);
                    }
                    return user;
                } else {
                    break;
                }
            }
        }
        throw new IllegalArgumentException("E-Mail oder Passwort falsch");
    }

    public User updateUserProfile(String userId, String firstName, String lastName, String newPassword, String address) {
        AppState state = loadState();
        User user = state.getUser(userId);
        if(user == null) {
            throw new IllegalArgumentException("Benutzer nicht gefunden");
        }
        if(firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Vorname darf nicht leer sein.");
        }
        if(lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Nachname darf nicht leer sein.");
        }
        if(address == null || address.isBlank()) {
            throw new IllegalArgumentException("Adresse darf nicht leer sein.");
        }

        boolean addressChanged = !address.equals(user.getAddress());

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);

        if(newPassword != null && !newPassword.isBlank()) {
            String hashedPassword = PasswordHasher.hashPassword(newPassword);
            user.setPasswordHash(hashedPassword);
        }

        if(addressChanged) {
           GeocodingService geocoder = new GeocodingService();
           GeocodingService.Coordinates coords = geocoder.geocodeAddress(address);
            if(coords != null) {
                user.setLatitude(coords.lat);
                user.setLongitude(coords.lon);
            }
        }

        state.saveUser(user);

        return user;
    }
}
