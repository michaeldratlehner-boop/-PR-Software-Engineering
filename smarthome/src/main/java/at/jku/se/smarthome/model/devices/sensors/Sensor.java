package at.jku.se.smarthome.model.devices.sensors;

import at.jku.se.smarthome.model.devices.SmartDevice;

public abstract class Sensor extends SmartDevice {
    protected String unit;   // z.B. "LUX", "°C", "%"
    protected double value;  // gemessener Wert
    protected Sensor() {
        super();
    }

    protected Sensor(String name, String unit, String roomId) {
        super(name, roomId);
        this.unit = unit;
        this.value = 0.0;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
