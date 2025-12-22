package at.jku.se.State;

import at.jku.se.smarthome.model.Room;

public class CurrentRoom {
    private static Room currentRoom;

    public static Room getCurrentRoom() { return currentRoom; }
    public static void setCurrentRoom(Room room) { currentRoom = room; }
}
