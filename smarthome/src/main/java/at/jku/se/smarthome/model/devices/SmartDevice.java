package at.jku.se.smarthome.model.devices;

import java.util.UUID;

public abstract class SmartDevice {
    protected String name;
    protected String id;
    protected boolean isOn;
    protected String roomId;

    public SmartDevice(String name, String roomId) {
        this.name = name;
        this.isOn = false; // Standardmäßig aus
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
    }

    public String getName() { return name; }
    public boolean isOn() { return isOn; }

    public String getRoomId() {return "";} //Implementierung fehlt noch

    public String getId() { return id; }


    // Jedes Gerät muss sagen können, wie sein Status ist
    public abstract String getStatus();
}