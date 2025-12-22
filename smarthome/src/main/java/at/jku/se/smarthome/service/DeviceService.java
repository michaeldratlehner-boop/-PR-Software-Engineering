package at.jku.se.smarthome.service;

import at.jku.se.State.AppState;
import at.jku.se.State.JsonStateService;
import at.jku.se.smarthome.model.devices.SmartDevice;

import java.util.ArrayList;
import java.util.List;

public class DeviceService {
    private final JsonStateService json = JsonStateService.getInstance();

    private AppState load() {
        return json.load();
    }

    private void save(AppState state) {
        json.save(state);
    }

    public List<SmartDevice> getDevicesInRoom(String roomId) {
        AppState state = load();
        List<SmartDevice> result = new ArrayList<>();
        for (SmartDevice d : state.getAllDevices()) { // falls Methode anders heißt: anpassen
            if (roomId != null && roomId.equals(d.getRoomId())) result.add(d);
        }
        return result;
    }

    public List<SmartDevice> getUnassignedDevices() {
        AppState state = load();
        List<SmartDevice> result = new ArrayList<>();
        for (SmartDevice d : state.getAllDevices()) {
            String rid = d.getRoomId();
            if (rid == null || rid.isBlank()) result.add(d);
        }
        return result;
    }

    public void assignDeviceToRoom(String deviceId, String roomId) {
        AppState state = load();
        SmartDevice d = state.getDevice(deviceId); // falls du so eine Methode hast
        if (d == null) throw new IllegalArgumentException("Gerät nicht gefunden");
        d.setRoomId(roomId);
        state.saveDevice(d); // oder state.save() je nach Aufbau
        save(state);
    }

    public void unassignDevice(String deviceId) {
        AppState state = load();
        SmartDevice d = state.getDevice(deviceId);
        if (d == null) throw new IllegalArgumentException("Gerät nicht gefunden");
        d.setRoomId(null);
        state.saveDevice(d);
        save(state);
    }
}
