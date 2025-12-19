package at.jku.se.smarthome.model.devices.sensors;

import at.jku.se.smarthome.model.devices.Sensor;

public class TemperatureSensor  extends Sensor {
    public TemperatureSensor(String name, String roomId) {
        super(name, "°C",  roomId);
    }
    public TemperatureSensor(String name, String roomId,double startValue){
        super(name, "°C",   roomId);
        this.value = startValue;
    }
}
