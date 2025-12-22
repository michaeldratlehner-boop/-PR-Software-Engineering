package at.jku.se.smarthome.model.devices.actors;

import at.jku.se.smarthome.model.devices.SmartDevice;

public abstract class Actor extends SmartDevice {

    protected Actor() {
        super();
    }

    protected Actor(String name, String roomId) {
        super(name, roomId);
    }

    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }

    public void toggle() {
        this.isOn = !this.isOn;
    }
}
