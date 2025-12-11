package at.jku.se.smarthome.model;

public class Room {
    private String name;
    private double sizeSquareMeters;
    private String id;

    public Room(String name, double sizeSquareMeters) {
        this.name = name;
        this.sizeSquareMeters = sizeSquareMeters;
    }

    public String getName() { return name; }
    public double getSizeSquareMeters() { return sizeSquareMeters; }

    public Object getHouseId() {
        return 0;
    }

    public String getId() {
        return "";
    }
}