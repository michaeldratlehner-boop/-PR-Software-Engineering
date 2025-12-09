package at.jku.se.State;
import at.jku.se.smarthome.model.User;
public class CurrentUser {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void clearCurrentUser() {
        currentUser = null;
    }
}
