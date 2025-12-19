package at.jku.se.smarthome.factory;
import at.jku.se.smarthome.model.devices.sensors.*;
import at.jku.se.smarthome.model.devices.SmartDevice;
import at.jku.se.smarthome.model.devices.actors.*;

public class DeviceFactory {
    public static SmartDevice create(
            String kind,
            String name,
            String roomId,
            double settedTemp,
            LightSensor lightSensor,
            double brightnessVerge,
            LightSensorType lightSensorType

    ){
        return switch (kind){
            case "Temperatursensor" -> new TemperatureSensor(name, roomId);
            case "Lichtsensor" -> new LightSensor(name, lightSensorType,roomId);
            case "Bewegungsmelder" -> new MotionSensor(name, roomId);
            case "Rauchmelder" -> new SmokeSensor(name, roomId);

            case "Steckdose" -> new PowerOutlet(name, roomId);
            case "Lichtsteuerung" -> new LightControl(name, roomId);
            case "Rolladensteuerung" -> new ShutterControl(name, lightSensor, brightnessVerge, roomId);
            case "Heizungssteuerung" -> new Heating(name,settedTemp,roomId);
            case "Türsteuerung" -> new DoorControl(name, roomId);
            case "Alarmsystem" -> new AlarmSystem(name, roomId);

            default -> throw new IllegalArgumentException("Invalid kind");
        };
    }
}
