package at.jku.se.smarthome.model.devices.sensors;

public class MotionSensor extends Sensor {
    private boolean motionDetected;
    public MotionSensor(String name, String roomId) {
        super(name, "", roomId);
        this.motionDetected = false;
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }
    public void setMotionDetected(boolean motionDetected) {
        this.motionDetected = motionDetected;
    }
    @Override
    public String getStatus(){
        return "Bewegungssensor " + name + ": " + (motionDetected ? "Bewegung erkannt" : "Keine Bewegung");
    }
}
