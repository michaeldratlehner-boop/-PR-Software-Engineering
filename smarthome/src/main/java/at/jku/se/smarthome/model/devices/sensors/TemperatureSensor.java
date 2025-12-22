package at.jku.se.smarthome.model.devices.sensors;

public class TemperatureSensor  extends Sensor {
    public TemperatureSensor(String name, String roomId) {
        super(name, "°C",  roomId);
    }

    @Override
    public String getStatus() {
        return "";
    }

    public TemperatureSensor(String name, String roomId,double startValue){
        super(name, "°C",   roomId);
        this.value = startValue;
    }
}
