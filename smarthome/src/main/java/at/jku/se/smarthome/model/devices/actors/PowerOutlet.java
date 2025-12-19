package at.jku.se.smarthome.model.devices.actors;

import at.jku.se.smarthome.model.devices.Actor;

public class PowerOutlet extends Actor {
    public PowerOutlet(String name, String roomId) {
        super(name, roomId);
    }

    public void  powerOn(){
        turnOn();
    }
    public void  powerOff(){
        turnOff();
    }
    @Override
    public String getStatus(){
        return "Steckdose " + name + ": " + (isOn ? "AN" : "AUS");
    }
}
