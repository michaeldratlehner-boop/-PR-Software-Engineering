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
    private Map<String, Map<String, String>> houseShares = new  HashMap<>();

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

    public Map<String, SmartDevice> getSensors() {
        return sensors;
    }

    public void setSensors(Map<String, SmartDevice> sensors) {
        this.sensors = sensors;
    }

    public Collection<SmartDevice> getAllSensors() {
        return sensors.values();
    }


    public Map<String, SmartDevice> getActors() {
        return actors;
    }

    public void setActors(Map<String, SmartDevice> actors) {
        this.actors = actors;
    }


    public Map<String, Map<String, String>> getHouseShares() {
        return houseShares;
    }

    public void setHouseShares(Map<String, Map<String, String>> houseShares) {
        this.houseShares = houseShares;
    }








}