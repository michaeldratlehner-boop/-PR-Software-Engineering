package at.jku.se.query;

import at.jku.se.State.AppState;
import at.jku.se.State.CurrentHouse;
import at.jku.se.State.JsonStateService;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.model.devices.SmartDevice;

import java.util.UUID;

public class AppStateMutations {
    private final AppState state;
    private final AppStateQuery query;
    public AppStateMutations(AppState state) {
        this.state = state;
        this.query = new AppStateQuery(state);
    }

    public void persist(){
        JsonStateService.getInstance().save(state);
    }

    //User mutations
    public void saveUser(User user) {
        state.getUsers().put(user.getId(), user);
        persist();
    }
    public void deleteUser(String id) {
        state.getUsers().remove(id);
        persist();
    }
    //House mutations
    public void saveHouse(House house) {
        state.getHouses().put(house.getId(), house);
        persist();
    }

    public void deleteHouse(String id) {
        state.getHouses().remove(id);
        persist();
    }
    //Room mutations
    public void saveRoom(Room room) {
        if (room == null) return;

        // ID erzwingen
        if (room.getId() == null || room.getId().isBlank()) {
            room.setId(UUID.randomUUID().toString());
        }

        // houseId erzwingen (wenn du CurrentHouse nutzt)
        if (room.getHouseId() == null || room.getHouseId().isBlank()) {
            if (CurrentHouse.getCurrentHouse() != null) {
                room.setHouseId(CurrentHouse.getCurrentHouse().getId());
            }
        }

        state.getRooms().put(room.getId(), room);
        persist();
    }



    public void deleteRoom(String id) {
        if (id == null) return;

        for (SmartDevice d : query.getAllDevices()) {
            if (id.equals(d.getRoomId())) {
                d.setRoomId(null);
            }
        }

        state.getRooms().remove(id);
        persist();
    }
    //Sensor mutations
    public void saveSensor(SmartDevice sensor) {
        state.getSensors().put(sensor.getId(), sensor);
        persist();
    }

    public void deleteSensor(String id) {
        state.getSensors().remove(id);
        persist();
    }
    //Actor mutations
    public void saveActor(SmartDevice actor) {
        state.getActors().put(actor.getId(), actor);
        persist();
    }

    public void deleteActor(String id) {
        state.getActors().remove(id);
        persist();
    }
    //Device mutations
    public void saveDevice(SmartDevice d) {
        if (d == null) return;

        // wenn schon in sensors-map vorhanden → als Sensor speichern
        if (state.getSensors().containsKey(d.getId())) {
            saveSensor(d);
            return;
        }

        // wenn schon in actors-map vorhanden → als Actor speichern
        if (state.getActors().containsKey(d.getId())) {
            saveActor(d);
            return;
        }

        // sonst: heuristisch nach Package (funktioniert bei dir sehr gut)
        String pkg = d.getClass().getPackageName();
        if (pkg.contains(".sensors")) saveSensor(d);
        else if (pkg.contains(".actors")) saveActor(d);
        else throw new IllegalArgumentException("Unbekannter Gerätetyp: " + d.getClass());
    }

}
