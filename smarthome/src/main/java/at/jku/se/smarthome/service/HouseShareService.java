package at.jku.se.smarthome.service;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentHouse;
import at.jku.se.State.JsonStateService;
import at.jku.se.smarthome.model.User;

import java.util.List;
import java.util.Map;

public class HouseShareService {
    private final JsonStateService jsonStateService = JsonStateService.getInstance();

    private AppState loadState() {
        return jsonStateService.load();
    }
    public void shareHouseWithUser(String userEmail) {
        AppState state = loadState();
        String houseId = CurrentHouse.getCurrentHouse().getId();

        User user = findUserByEmail(state, userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        state.getHouseShares().putIfAbsent(houseId, new java.util.HashMap<>());
        Map<String, String> shares = state.getHouseShares().get(houseId);

        if (shares.containsKey(user.getId())) {
            throw new IllegalArgumentException("House already shared with this user");
        }
        shares.put(user.getId(), java.time.Instant.now().toString());
        saveState(state);
    }

    public List<String> getSharedUserEmails(){
        AppState state = loadState();
        String houseId = CurrentHouse.getCurrentHouse().getId();
        return state.getSharedUsersEmailsForHouse(houseId);
    }
    public void unshareHouseWithUser(String userEmail) {
        AppState state = loadState();
        String houseId = CurrentHouse.getCurrentHouse().getId();

        User user = findUserByEmail(state, userEmail);
        if (user == null) {
            return;
        }

        Map<String, String> shares = state.getHouseShares().get(houseId);
        if (shares == null ) {
            return;
        }

        shares.remove(user.getId());

        if(shares.isEmpty()) {
            state.getHouseShares().remove(houseId);
        }
        saveState(state);
    }

    private void saveState(AppState state) {
        jsonStateService.save(state);
    }
    private User findUserByEmail(AppState state, String email) {
        String normalizedEmail = email.trim().toLowerCase();

        for (User user : state.getUsers().values()) {
            if (user.getEmail().equalsIgnoreCase(normalizedEmail)) {
                return user;
            }
        }
        return null;
    }

}
