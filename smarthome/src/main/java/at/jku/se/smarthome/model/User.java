package at.jku.se.smarthome.model;
import java.util.UUID;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;     // Dient als ID beim Login
    private String passwordHash;
    private String address;
    private String houseId;
    private Double latitude;
    private Double longitude;
    private String avatarPath; // z.B. "avatars/<userId>.png"
    // Konstruktor
    public User(){}//State Constructor

    public User(String firstName, String lastName, String email, String passwordHash, String address) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.address = address;
    }

    // Getter und Setter
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseId() {
        return houseId;
    }
    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }

}