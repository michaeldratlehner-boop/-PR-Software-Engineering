package at.jku.se.JSON;

import at.jku.se.smarthome.model.House;
import at.jku.se.smarthome.model.Room;
import at.jku.se.smarthome.model.User;

import java.util.*;

public class AppState {
    private Map<String, User> users = new HashMap<>();
    private Map<String, House> houses = new HashMap<>();
    private Map<String, Room> rooms = new HashMap<>();

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
    public void saveHouse(House house) {
        houses.put(house.getId(), house);
        save();
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
    // Save the current state to JSON
    private void save() {
        JsonStateService.getInstance().save(this);
    }
}
