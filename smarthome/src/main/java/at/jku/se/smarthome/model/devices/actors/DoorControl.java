package at.jku.se.smarthome.model.devices.actors;

import at.jku.se.smarthome.model.devices.Actor;
import at.jku.se.smarthome.model.devices.sensors.DoorState;

public class DoorControl extends Actor {
    private DoorState doorState;
    public DoorControl(String name, String roomId) {
        super(name,  roomId);
        this.doorState = DoorState.CLOSED;
    }
    public DoorState getDoorState() {
        return doorState;
    }
    public void open(){
        if(doorState != DoorState.LOCKED){
            doorState = DoorState.OPEN;
            isOn = true;
        }
    }
    public void close(){
        doorState = DoorState.CLOSED;
        isOn = false;
    }
    public void unlock(){
        if(doorState == DoorState.LOCKED){
            doorState = DoorState.CLOSED;
        }
    }
    public void lock(){
        doorState = DoorState.LOCKED;
        isOn = true;
    }
    @Override
    public String getStatus(){
        return "DoorControl" + name + ": " + doorState;
    }
}
