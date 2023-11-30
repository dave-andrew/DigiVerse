package net.slc.dv.helper;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

    public static ArrayList<Image> spider(String baseString) {
        ArrayList<Image> sprites = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
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

    public static Image importGUI(String baseString) {
        return new Image("file:resources/game/gui/" + baseString + ".png");
    }

    public static void makeCircular(ImageView imageView, double radius) {

        Circle clip = new Circle();
        clip.setCenterX(radius);
        clip.setCenterY(radius);
        clip.setRadius(radius);
        imageView.setClip(clip);

        imageView.setPreserveRatio(false);

        imageView.setFitWidth(radius * 2);
        imageView.setFitHeight(radius * 2);
    }

    public static void makeCircular(HBox container, ImageView imageView, double radius) {

        StackPane stackPane = new StackPane();

        Circle clip = new Circle();
        clip.setCenterX(radius);
        clip.setCenterY(radius);
        clip.setRadius(radius);
        clip.getStyleClass().add("profile-pic");

        stackPane.getChildren().add(clip);

        Circle clip2 = new Circle();
        clip2.setCenterX(radius);
        clip2.setCenterY(radius);
        clip2.setRadius(radius - 7);

        imageView.setPreserveRatio(false);

        imageView.setFitWidth(radius * 2);
        imageView.setFitHeight(radius * 2);
        imageView.setClip(clip2);

        stackPane.getChildren().add(imageView);

        stackPane.setAlignment(Pos.CENTER);

        container.getChildren().add(stackPane);

    }

}
