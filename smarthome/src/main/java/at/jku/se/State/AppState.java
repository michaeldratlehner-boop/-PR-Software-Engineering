package at.jku.se.State;

import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.model.devices.*;

import java.util.*;

public class AppState {
    private Map<String, User> users = new HashMap<>();
    private Map<String, House> houses = new HashMap<>();
    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Sensor> sensors = new HashMap<>();
    private Map<String, Object> actors = new HashMap<>();

    public AppState() {
    }

    //Getter and Setter for the AppState class
    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public Map<String, House> getHouses() {
        return houses;
    }

    public void setHouses(Map<String, House> houses) {
        this.houses = houses;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    public Map<String, Sensor> getSensors() {return sensors;}

    public void setSensors(Map<String, Sensor> sensors) {this.sensors = sensors;}

    public Map<String, Object> getActors() {return actors;}

    public void setActors(Map<String, Object> actors) {this.actors = actors;}

    //User methods
    public User getUser(String id) {
        return users.get(id);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public void saveUser(User user) {
        users.put(user.getId(), user);
        save();
    }

    public void deleteUser(String id) {
        users.remove(id);
        save();
    }
    //House methods
    public House getHouse(String id) {
        return houses.get(id);
    }

    public Collection<House> getAllHouses() {
        return houses.values();
    }

    public List<House> getAllHousesForUser(User user) {
        List<House> result = new ArrayList<>();

        if(user.getHouseId() != null) {
            House house = houses.get(user.getHouseId());
            if(house != null) {
                result.add(house);
            }
        }
        return result;
    }

    public void saveHouse(House house) {
        houses.put(house.getId(), house);
        save();
    }

    public String nextHouseId() {
        long maxId = 0;
        for (String key : houses.keySet()) {
            if (key != null && key.matches("\\d+")) {
                long id = Long.parseLong(key);
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return Long.toString(maxId + 1);
    }

    public void deleteHouse(String id) {
        houses.remove(id);
        save();
    }
    //Room methods
    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public List<Room> getRoomsByHouseId(String houseId) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getHouseId().equals(houseId)) {
                result.add(room);
            }
        }
        return result;
    }
    public void saveRoom(Room room) {
        rooms.put(room.getId(), room);
        save();
    }
    public void deleteRoom(String id) {
        rooms.remove(id);
        save();
    }

    //Sensor methods
    public Sensor getSensor(String id) {
        return sensors.get(id);
    }

    public Collection<Sensor> getAllSensors() {
        return sensors.values();
    }

    public Collection<Sensor> getAllSensorsByRoomId(String roomId) {
        List<Sensor> result = new ArrayList<>();
        for (Sensor sensor : sensors.values()) {
            if(sensor.getRoomId().equals(roomId)) {
                result.add(sensor);
            }
        }
        return result;
    }

    public void saveSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        save();
    }

    public void deleteSensor(String id) {
        sensors.remove(id);
        save();
    }

    //Actor methods
    public Object getActor(String id) {
        return actors.get(id);
    }

    public Collection<Object> getAllActors() {
        return actors.values();
    }

    public void saveActor(Actor actor) {
        actors.put(actor.getId(), actor);
        save();
    }

    public void deleteActor(String id) {
        actors.remove(id);
        save();
    }


    // Save the current state to State
    private void save() {
        JsonStateService.getInstance().save(this);
    }
}
