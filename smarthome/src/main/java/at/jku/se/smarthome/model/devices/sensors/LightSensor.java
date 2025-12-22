package at.jku.se.smarthome.model.devices.sensors;

public class LightSensor extends Sensor {
    private LightSensorType type;
    public LightSensor(String name, LightSensorType type, String roomId) {
        super(name, "Lux", roomId);
        this.type = type;
    }

    public LightSensorType getType() {
        return type;
    }
    public boolean isBright(){
        if(type == LightSensorType.OUTDOOR){
            return value > 20000;
        }else{
            return value > 300;
        }
    }

    public String getStatus(){
        return "Lichtsensor " + name + "(" + type + "): " + value + " Lux";
    }
}
