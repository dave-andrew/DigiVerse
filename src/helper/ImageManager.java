package helper;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImageManager {

    public static Image convertBlobImage(Blob blob) {

        try {
            byte[] blobData = blob.getBytes(1, (int) blob.length());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(blobData);

            return new Image(inputStream);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Image> importPlayerSprites(String baseString) {
        ArrayList<Image> sprites = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            sprites.add(new Image("file:resources/game/player/" + baseString + "-" + i + ".png"));
        }
        return sprites;
    }

    public static ArrayList<Image> importSprites(String baseString) {
        ArrayList<Image> sprites = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            sprites.add(new Image("file:resources/game/" + baseString + "-" + i + ".png"));
        }
        return sprites;
    }

    public static ArrayList<Image> importDeadSprites(String baseString) {
        ArrayList<Image> sprites = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            sprites.add(new Image("file:resources/game/died/" + baseString + "-" + i + ".png"));
        }
        return sprites;
    }

}
