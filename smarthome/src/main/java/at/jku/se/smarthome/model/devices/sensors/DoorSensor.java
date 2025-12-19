package at.jku.se.smarthome.model.devices.sensors;

import at.jku.se.smarthome.model.devices.Sensor;

public class DoorSensor extends Sensor{
    private DoorState doorState;
    public DoorSensor(String name, String roomId) {
        super(name, "",  roomId);
        this.doorState = DoorState.CLOSED;
    }

    public DoorState getDoorState() {
        return doorState;
    }
    public void setDoorState(DoorState doorState) {
        this.doorState = doorState;
    }

    public String getStatus(){
        return "Türsensor " + name + " ist " + doorState;
    }
}
