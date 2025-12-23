package at.jku.se.smarthome.service;

import at.jku.se.State.AppState;
import at.jku.se.State.JsonStateService;
import at.jku.se.query.AppStateMutations;
import at.jku.se.query.AppStateQuery;
import at.jku.se.smarthome.model.devices.SmartDevice;

import java.util.ArrayList;
import java.util.List;

public class DeviceService {
    private final AppState appState = AppState.getInstance();
    private final AppStateQuery appStateQuery = new AppStateQuery(appState);
    private final AppStateMutations appStateMutations = new AppStateMutations(appState);

    public List<SmartDevice> getDevicesInRoom(String roomId) {
        List<SmartDevice> result = new ArrayList<>();
        for (SmartDevice d : appStateQuery.getAllDevices()) { // falls Methode anders heißt: anpassen
            if (roomId != null && roomId.equals(d.getRoomId())) result.add(d);
        }
        return result;
    }

    public List<SmartDevice> getUnassignedDevices() {
        List<SmartDevice> result = new ArrayList<>();
        for (SmartDevice d : appStateQuery.getAllDevices()) {
            String rid = d.getRoomId();
            if (rid == null || rid.isBlank()) result.add(d);
        }
        return result;
    }

    public void assignDeviceToRoom(String deviceId, String roomId) {
        SmartDevice d = appStateQuery.getDevice(deviceId); // falls du so eine Methode hast
        if (d == null) throw new IllegalArgumentException("Gerät nicht gefunden");
        d.setRoomId(roomId);
        appStateMutations.saveDevice(d);
    }

    public void unassignDevice(String deviceId) {
        SmartDevice d = appStateQuery.getDevice(deviceId);
        if (d == null) throw new IllegalArgumentException("Gerät nicht gefunden");
        d.setRoomId(null);
        appStateMutations.saveDevice(d);
    }
}
