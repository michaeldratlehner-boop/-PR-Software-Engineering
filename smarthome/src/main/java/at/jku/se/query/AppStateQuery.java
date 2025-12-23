package at.jku.se.query;

import at.jku.se.State.AppState;
import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.model.devices.SmartDevice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AppStateQuery {
    private final AppState state;

    public AppStateQuery(AppState state) {
        this.state = state;
    }

    //Simple read operations
    public User getUser(String id) {
        return state.getUsers().get(id);
    }

    public House getHouse(String id) {
        return state.getHouses().get(id);
    }
    public Room getRoom(String id) {
        return state.getRooms().get(id);
    }

    public SmartDevice getSensor(String id) {
        return state.getSensors().get(id);
    }

    public SmartDevice getActor(String id) {
        return state.getActors().get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(state.getUsers().values());
    }




    //Query operations
    public List<House> getAllHousesForUser(User user) {
        List<House> result = new ArrayList<>();
        if (user.getHouseId() != null) {
            House house = state.getHouses().get(user.getHouseId());
            if (house != null) result.add(house);
        }
        return result;

    }
    public List<Room> getAllRooms() {
        return new ArrayList<>(state.getRooms().values());
    }

    public List<Room> getRoomsByHouseId(String houseId) {
        List<Room> result = new ArrayList<>();
        for (Room room : state.getRooms().values()) {
            if (houseId != null && houseId.equals(room.getHouseId())) {
                result.add(room);
            }
        }
        return result;
    }
    public List<SmartDevice> getSensorsByRoomId(String roomId) {
        List<SmartDevice> result = new ArrayList<>();
        if(roomId==null) {
            return result;
        }

        for (SmartDevice sensor : state.getSensors().values()) {
            if (sensor != null && roomId.equals(sensor.getRoomId())) {
                result.add(sensor);
            }
        }
        return result;
    }

    public List<SmartDevice> getActorByRoomId(String roomId) {
        List<SmartDevice> result = new ArrayList<>();
        if(roomId==null) {
            return result;
        }

        for (SmartDevice actor : state.getActors().values()) {
            if (actor != null && roomId.equals(actor.getRoomId())) {
                result.add(actor);
            }
        }
        return result;
    }
    public List<SmartDevice> getAllDevices(){
        List<SmartDevice> result = new ArrayList<>();
        result.addAll(state.getSensors().values());
        result.addAll(state.getActors().values());
        return result;
    }

    public SmartDevice getDevice(String id) {
        if(id == null) {
            return null;
        }
        for(SmartDevice device : getAllDevices()) {
            if(id.equals(device.getId())) {
                return device;
            }
        }
        return null;
    }

    public List<String> getSharedUsersEmailsForHouse(String houseId) {
        List<String> result = new ArrayList<>();
        Map<String, String> shares = state.getHouseShares().get(houseId);
        if (shares != null) {
            for (String userId : shares.keySet()) {
                User user = state.getUsers().get(userId);
                if (user != null && user.getEmail() != null) {
                    result.add(user.getEmail());
                }
            }
        }
        return result;
    }

    //Count operations
    public int countSensorsForHouse(String houseId) {
        if(houseId == null) {
            return 0;
        }
        int count = 0;
        for (Room rooms: getRoomsByHouseId(houseId)) {
            count += getSensorsByRoomId(rooms.getId()).size();
        }
        return count;
    }


    public int countActorsForHouse(String houseId) {
        if(houseId == null) {
            return 0;
        }
        int count = 0;
        for (Room rooms: getRoomsByHouseId(houseId)) {
            count += getActorByRoomId(rooms.getId()).size();
        }
        return count;
    }
}
