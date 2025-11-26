package at.jku.se.smarthome.model.devices;

public abstract class Sensor extends SmartDevice {
    protected double value;
    protected String unit; // Einheit z.B. "°C"

    public Sensor(String name, String unit) {
        super(name);
        this.unit = unit;
        this.isOn = true; // Sensoren sind meist immer an
    }

    public double getValue() { return value; }

    // Simuliert das Messen eines neuen Wertes
    public void setValue(double value) { this.value = value; }

    @Override
    public String getStatus() {
        return "Sensor " + name + ": " + value + " " + unit;
    }
}