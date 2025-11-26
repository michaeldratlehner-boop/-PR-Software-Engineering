package at.jku.se.smarthome.model;

import java.util.ArrayList;
import java.util.List;

public class House {
    private String name;
    private String address;
    // Liste aller Räume im Haus
    private List<Room> rooms = new ArrayList<>();

    public House(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
}