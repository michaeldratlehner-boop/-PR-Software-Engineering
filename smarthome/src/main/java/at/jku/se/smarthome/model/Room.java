package at.jku.se.smarthome.model;

public class Room {
    private String name;
    private double sizeSquareMeters;
    private String id;
    private String houseId;

    public Room(String name, double sizeSquareMeters) {
        this.name = name;
        this.sizeSquareMeters = sizeSquareMeters;
    }

    public Room() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getSizeSquareMeters() { return sizeSquareMeters; }
    public void setSizeSquareMeters(double sizeSquareMeters) { this.sizeSquareMeters = sizeSquareMeters; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHouseId() { return houseId; }
    public void setHouseId(String houseId) { this.houseId = houseId; }
}