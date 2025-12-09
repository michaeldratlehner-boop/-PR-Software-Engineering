package at.jku.se.smarthome.service;
import org.mindrot.jbcrypt.BCrypt;
public class PasswordHasher {
    public static String hashPassword(String password) {
        if(password == null) {
            return null;
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    public static boolean checkPassword(String password, String hashed) {
        if(password == null || hashed == null) {
            return false;
        }
        return BCrypt.checkpw(password, hashed);
    }

}
