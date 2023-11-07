package helper;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImageManager {

    private static ArrayList<Image> longSpriteExtract(Image image, int totalFrame) {
        int sectionWidth = (int) (image.getWidth() / totalFrame);
        int frameHeight = (int) image.getHeight();
        ArrayList<Image> spriteContainer = new ArrayList<>();

        for (int section = 0; section < totalFrame; section++) {
            int startX = section * sectionWidth;
            int endX = startX + sectionWidth;

            WritableImage frameImage = new WritableImage(sectionWidth, frameHeight);
            for (int y = 0; y < frameHeight; y++) {
                for (int x = startX; x < endX; x++) {
                    Color pixelColor = image.getPixelReader().getColor(x, y);
                    frameImage.getPixelWriter().setColor(x - startX, y, pixelColor);
                }
            }
            spriteContainer.add(frameImage);
        }

        return spriteContainer;
    }

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

    public static ArrayList<Image> importEnemySprites(String baseString) {
        ArrayList<Image> sprites = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            sprites.add(new Image("file:resources/game/enemy/" + baseString + "-" + i + ".png"));
        }
        return sprites;
    }

    public static ArrayList<Image> importGroundSprites(String baseString) {
        ArrayList<Image> sprites = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            sprites.add(new Image("file:resources/game/ground/" + baseString + "-" + i + ".png"));
        }
        return sprites;
    }

    public static ArrayList<Image> importEnemyDiedSprites() {
        Image image = new Image("file:resources/game/died/enemy-dead.png");

        return longSpriteExtract(image, 6);
    }

}
