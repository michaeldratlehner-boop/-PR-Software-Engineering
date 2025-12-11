package at.jku.se.smarthome.model.devices;

public abstract class SmartDevice {
    protected String name;
    protected String id;
    protected boolean isOn;

    public SmartDevice(String name) {
        this.name = name;
        this.isOn = false; // Standardmäßig aus
    }

    public String getName() { return name; }
    public boolean isOn() { return isOn; }

    public String getRoomId() {return "";} //Implementierung fehlt noch

    public String getId() { return id; }


    // Jedes Gerät muss sagen können, wie sein Status ist
    public abstract String getStatus();
}