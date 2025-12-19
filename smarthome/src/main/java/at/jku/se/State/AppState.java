package at.jku.se.State;

import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.User;
import at.jku.se.smarthome.model.devices.SmartDevice;

import java.util.*;

public class AppState {
    private static AppState instance;

    private Map<String, User> users;
    private Map<String, House> houses;
    private Map<String, Room> rooms;
    private Map<String, SmartDevice> sensors;
    private Map<String, SmartDevice> actors;

    private AppState() {
        users = new HashMap<>();
        houses = new HashMap<>();
        rooms = new HashMap<>();
        sensors = new HashMap<>();
        actors = new HashMap<>();
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    static void setInstance(AppState loadedState) {
        instance = loadedState;
    }


    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

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

    public Map<String, House> getHouses() {
        return houses;
    }

    public void setHouses(Map<String, House> houses) {
        this.houses = houses;
    }

    public House getHouse(String id) {
        return houses.get(id);
    }

    public Collection<House> getAllHouses() {
        return houses.values();
    }

    public void saveHouse(House house) {
        houses.put(house.getId(), house);
        save();
    }

    public void deleteHouse(String id) {
        houses.remove(id);
        save();
    }

    public List<House> getAllHousesForUser(User user) {
        List<House> result = new ArrayList<>();
        if (user.getHouseId() != null) {
            House house = houses.get(user.getHouseId());
            if (house != null) result.add(house);
        }
        return result;
    }

    public String nextHouseId() {
        long maxId = 0;
        for (String key : houses.keySet()) {
            if (key != null && key.matches("\\d+")) {
                long id = Long.parseLong(key);
                if (id > maxId) maxId = id;
            }
        }
        return Long.toString(maxId + 1);
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public List<Room> getRoomsByHouseId(String houseId) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getHouseId().equals(houseId)) result.add(room);
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

    public Map<String, SmartDevice> getSensors() {
        return sensors;
    }

    public void setSensors(Map<String, SmartDevice> sensors) {
        this.sensors = sensors;
    }

    public SmartDevice getSensor(String id) {
        return sensors.get(id);
    }

    public Collection<SmartDevice> getAllSensors() {
        return sensors.values();
    }

    public Collection<SmartDevice> getAllSensorsByRoomId(String roomId) {
        List<SmartDevice> result = new ArrayList<>();
        for (SmartDevice sensor : sensors.values()) {
            if (sensor.getRoomId().equals(roomId)) result.add(sensor);
        }
        return result;
    }

    public void saveSensor(SmartDevice sensor) {
        sensors.put(sensor.getId(), sensor);
        save();
    }

    public void deleteSensor(String id) {
        sensors.remove(id);
        save();
    }

    public Map<String, SmartDevice> getActors() {
        return actors;
    }

    public void setActors(Map<String, SmartDevice> actors) {
        this.actors = actors;
    }

    public SmartDevice getActor(String id) {
        return actors.get(id);
    }

    public Collection<SmartDevice> getAllActors() {
        return actors.values();
    }

    public void saveActor(SmartDevice actor) {
        actors.put(actor.getId(), actor);
        save();
    }

    public void deleteActor(String id) {
        actors.remove(id);
        save();
    }

    private void save() {
        JsonStateService.getInstance().save(this);
    }
}