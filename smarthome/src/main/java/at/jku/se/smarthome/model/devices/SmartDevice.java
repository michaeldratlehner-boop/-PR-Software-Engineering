package at.jku.se.smarthome.model.devices;

import java.util.UUID;

public abstract class SmartDevice {
    protected String name;
    protected String id;
    protected boolean isOn;
    protected String roomId; // null = nicht zugeordnet

    // Für Gson: No-Args Constructor (optional, aber oft hilfreich)
    protected SmartDevice() {}

    public SmartDevice(String name, String roomId) {
        this.name = name;
        this.isOn = false;
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
    }

    public String getName() { return name; }
    public boolean isOn() { return isOn; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getId() { return id; }

    public abstract String getStatus();
}
