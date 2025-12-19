package at.jku.se.smarthome.model.devices.sensors;

import at.jku.se.smarthome.model.devices.Sensor;

public class WindowSensor extends Sensor {
    public WindowState windowState;
    public WindowSensor(String name, String roomId) {
        super(name, "", roomId);
        this.windowState = WindowState.GESCHLOSSEN;
        this.isOn = true;
    }
    public WindowState getWindowState() {
        return windowState;
    }
    public void setWindowState(WindowState windowState) {
        this.windowState = windowState;
    }
    public boolean isAlarm(){
        return windowState==WindowState.GLAS_GEBROCHEN || windowState==WindowState.OFFEN;
    }
    @Override
    public String getStatus(){
        return "Fenster " + name + ": " + windowState + (isAlarm() ? " (Alarm)" : "");
    }
}
