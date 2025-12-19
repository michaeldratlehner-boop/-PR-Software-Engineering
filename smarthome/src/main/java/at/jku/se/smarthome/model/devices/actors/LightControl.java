package at.jku.se.smarthome.model.devices.actors;

import at.jku.se.smarthome.model.devices.Actor;

public class LightControl extends Actor{
    private int brightness;
    public LightControl(String name, String roomId) {
        super(name, roomId);
        this.brightness = 0;
    }
    public void setBrightness(int brightness){
        this.brightness = Math.max(0, Math.min(100, brightness));
        this.isOn = brightness > 0;
    }
    public int getBrightness(){
        return this.brightness;
    }
    public void autoAdjust(double lightLevel){
        if (lightLevel < 20){
            setBrightness(100);
        }else if (lightLevel < 50){
            setBrightness(60);
        }else {
            setBrightness(0);
        }
    }
    @Override
    public String getStatus(){
        return "Licht " + name + ": " + (isOn ? "AN (" + brightness + "%)" : "AUS");
    }
}
