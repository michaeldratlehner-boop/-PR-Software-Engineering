package at.jku.se.smarthome.model.devices.actors;

import at.jku.se.smarthome.model.devices.Actor;
import at.jku.se.smarthome.model.devices.sensors.LightSensor;

public class ShutterControl extends Actor {
    private ShutterState shutterState;
    private final double brightnessVerge;
    private final LightSensor lightSensor;

    public ShutterControl(String name, LightSensor lightSensor, double brightnessVerge, String roomId) {
        super(name, roomId);
        this.lightSensor = lightSensor;
        this.brightnessVerge = brightnessVerge;
        this.shutterState = ShutterState.OFFEN;
    }
    public void update(){
        if(!isOn) return;
        double lux = lightSensor.getValue();

        if(lux > brightnessVerge && shutterState!= ShutterState.GESCHLOSSEN){
            moveDown();
        } else if (lux <= brightnessVerge && shutterState != ShutterState.OFFEN) {
            moveUp();
        }
    }
    public void moveUp(){
        shutterState = ShutterState.FAHRT_HOCH;
        shutterState = ShutterState.OFFEN;
    }
    public void moveDown(){
        shutterState = ShutterState.FAHRT_RUNTER;
        shutterState = ShutterState.GESCHLOSSEN;
    }
    public ShutterState getShutterState() {
        return shutterState;
    }
    @Override
    public String getStatus(){
        return "Rolladen " + name + ": " + shutterState;
    }
}
