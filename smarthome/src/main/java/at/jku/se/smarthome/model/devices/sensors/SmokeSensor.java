package at.jku.se.smarthome.model.devices.sensors;

public class SmokeSensor extends Sensor {
    private boolean smokeDetected;
    public SmokeSensor(String name, String roomId) {
        super(name, "", roomId);
        this.smokeDetected = false;
    }

    public void setSmokeDetected(boolean smokeDetected) {
        this.smokeDetected = true;
    }
    public boolean getSmokeDetected() {
        return this.smokeDetected;
    }
    public boolean isSmokeDetected() {
        return this.smokeDetected == true;
    }
    @Override
    public String getStatus(){
        return "Rauchmelder " + name + ": " + (isSmokeDetected()? "Alarm!" : "Kein Rauch");
    }
}
