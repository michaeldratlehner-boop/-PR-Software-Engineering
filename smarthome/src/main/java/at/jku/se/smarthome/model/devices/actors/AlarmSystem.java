package at.jku.se.smarthome.model.devices.actors;

public class AlarmSystem extends Actor {
    private AlarmState alarmState;
    public AlarmSystem(String name,  String roomId) {
        super(name, roomId);
        this.alarmState = AlarmState.DEAKTIVIERT;
    }
    public void activateAlarm() {
        this.alarmState = AlarmState.SCHARF;
        this.isOn = true;
    }
    public void deactivateAlarm() {
        this.alarmState = AlarmState.DEAKTIVIERT;
        this.isOn = false;
    }
    public void alarm(String cause){
        if(alarmState == AlarmState.SCHARF){
            alarmState = AlarmState.ALARM;
            System.out.println("Alarm! Grund + " + cause);
        }
    }
    public void resetAlarm(){
        if (alarmState == AlarmState.ALARM){
            alarmState = AlarmState.SCHARF;
        }
    }
    public AlarmState getAlarmState() {
        return alarmState;
    }
    @Override
    public String getStatus(){
        return "Alarmsystem " + name + ": " + alarmState;
    }
}
