package at.jku.se.smarthome.model.devices.sensors;

public enum DoorState {
    OPEN("offen"),
    CLOSED("geschlossen"),
    LOCKED("versperrt");

    private final String label;
    private DoorState(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
