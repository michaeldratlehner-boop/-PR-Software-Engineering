package at.jku.se.smarthome.model.devices.actors;

import at.jku.se.smarthome.model.devices.Actor;
import at.jku.se.smarthome.model.devices.sensors.TemperatureSensor;

public class Heating extends Actor {
    private double settedTemperature;
    private HeatingMode heatingMode;
    public Heating(String name, double settedTemperature, String roomId) {
        super(name,  roomId);
        this.settedTemperature = settedTemperature;
        this.heatingMode = HeatingMode.AUS;
    }
    public double getSettedTemperature() {
        return settedTemperature;
    }
    public void setSettedTemperature(double settedTemperature) {
        this.settedTemperature = settedTemperature;
    }
    public HeatingMode getHeatingMode() {
        return heatingMode;
    }
    public void setHeatingMode(HeatingMode heatingMode) {
        this.heatingMode = heatingMode;
        this.isOn = heatingMode != HeatingMode.AUS;
    }
    public void regulate(TemperatureSensor sensor){
        if(heatingMode == HeatingMode.AUS){
            turnOff();
            return;
        }
        if(sensor.getValue() <= settedTemperature){
            turnOn();
        }else {
            turnOff();
        }
    }
    @Override
    public String getStatus(){
        return "Zentralheizung " + name +
                " | Modus: " + heatingMode +
                " | Ziel: " + settedTemperature + " °C" +
                " | Status: " + (isOn ? "HEIZT" : "AUS");
    }
}
