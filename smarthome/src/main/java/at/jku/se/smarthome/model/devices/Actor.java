package at.jku.se.smarthome.model.devices;

public abstract class Actor extends SmartDevice {

    public Actor(String name) {
        super(name);
    }

    public void turnOn() { this.isOn = true; }
    public void turnOff() { this.isOn = false; }

    @Override
    public String getStatus() {
        return name + " ist " + (isOn ? "AN" : "AUS");
    }

}