package at.jku.se.smarthome.model;

import java.util.ArrayList;
import java.util.List;

public class House {
    private String houseId;
    private String name;
    private String address;
    private int floors;
    private Double latitude;
    private Double longitude;


    public House() {
    }

    public House(String name, int floors, String address) {

        this.name = name;
        this.floors = floors;
        this.address = address;
    }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return address; }

    public void setId(String id) { this.houseId = id; }
    public String getId() {
        return houseId;
    }

    public void setFloors(int floors) { this.floors = floors; }
    public int getFloors() { return floors; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLatitude() { return latitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLongitude() { return longitude; }


}