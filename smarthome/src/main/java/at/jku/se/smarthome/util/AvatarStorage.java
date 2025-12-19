package at.jku.se.smarthome.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class AvatarStorage {

    private static final Path AVATAR_DIR = Paths.get("avatars");

    public static String storeAvatar(File sourceFile, String userId) {
        if (sourceFile == null) return null;

        try {
            Files.createDirectories(AVATAR_DIR);

            String ext = getExtension(sourceFile.getName());
            if (ext == null) ext = "png"; // fallback

            Path target = AVATAR_DIR.resolve(userId + "." + ext.toLowerCase());

            Files.copy(sourceFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

            // relativer Pfad, damit es auf anderen PCs auch klappt
            return AVATAR_DIR.getFileName().toString() + "/" + target.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException("Avatar konnte nicht gespeichert werden.", e);
        }
    }

    private static String getExtension(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) return null;
        return name.substring(dot + 1);
    }
}
