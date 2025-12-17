package at.jku.se.State;

import at.jku.se.smarthome.model.House;

public class CurrentHouse {
    private static House currentHouse;

    public static House getCurrentHouse() { return currentHouse;}

    public static void setCurrentHouse(House house) { currentHouse = house; }

    public static void clearCurrentHouse() { currentHouse = null; }
}
