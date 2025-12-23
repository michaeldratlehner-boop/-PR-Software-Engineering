package at.jku.se.smarthome.service;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentHouse;
import at.jku.se.State.JsonStateService;
import at.jku.se.query.AppStateMutations;
import at.jku.se.query.AppStateQuery;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseShareService {
    private final AppState appState = AppState.getInstance();
    private final AppStateQuery appStateQuery = new AppStateQuery(appState);
    private final AppStateMutations appStateMutations = new AppStateMutations(appState);

    public void shareHouseWithUser(String userEmail) {
        if (userEmail == null || userEmail.isBlank())
            throw new IllegalArgumentException("Email darf nicht leer sein");

        House current = CurrentHouse.getCurrentHouse();
        if (current == null) throw new IllegalStateException("Kein aktuelles Haus gewählt");
        String houseId = current.getId();

        User user = findUserByEmail(userEmail);
        if (user == null) throw new IllegalArgumentException("User not found");

        // Einladungen nur wenn User noch kein Haus hat (eure Regel)
        if (user.getHouseId() != null && !user.getHouseId().isBlank())
            throw new IllegalArgumentException("User hat bereits ein Haus");

        appState.getHouseShares().putIfAbsent(houseId, new HashMap<>());
        Map<String, String> shares = appState.getHouseShares().get(houseId);

        if (shares.containsKey(user.getId()))
            throw new IllegalArgumentException("House already shared with this user");

        shares.put(user.getId(), Instant.now().toString());

        appStateMutations.persist(); // <- falls bei dir anders heißt, ersetzen
    }

    public List<String> getSharedUserEmails() {
        House current = CurrentHouse.getCurrentHouse();
        if (current == null) return List.of();
        return appStateQuery.getSharedUsersEmailsForHouse(current.getId());
    }

    public void unshareHouseWithUser(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) return;

        House current = CurrentHouse.getCurrentHouse();
        if (current == null) return;
        String houseId = current.getId();

        User user = findUserByEmail(userEmail);
        if (user == null) return;

        Map<String, String> shares = appState.getHouseShares().get(houseId);
        if (shares == null) return;

        shares.remove(user.getId());
        if (shares.isEmpty()) appState.getHouseShares().remove(houseId);

        appStateMutations.persist(); // <- falls bei dir anders heißt, ersetzen
    }

    private User findUserByEmail(String email) {
        String normalized = email.trim().toLowerCase();
        for (User u : appState.getUsers().values()) {
            if (u.getEmail() != null && u.getEmail().trim().toLowerCase().equals(normalized)) {
                return u;
            }
        }
        return null;
    }
}
