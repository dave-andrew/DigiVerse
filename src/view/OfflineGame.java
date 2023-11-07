package view;

import game.Enemy;
import game.Player;
import helper.ImageManager;
import helper.InputManager;
import helper.ScreenManager;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class OfflineGame {

    private Player player;
    Pane root;
    private InputManager inputManager;

    private ArrayList<Enemy> enemyList = new ArrayList<>();

    private ArrayList<Image>  groundSprites = new ArrayList<>();

    private long lastTimeFrame = 0;
    public OfflineGame(Stage stage) {

        root = new Pane();
        Scene scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);

        inputManager = InputManager.getInstance(scene);

        player = Player.getInstance();
        player.setX(player.getPosX());
        player.setY(player.getPosY());

        groundSprites = ImageManager.importGroundSprites("tile");
        setupBackground();

        root.getChildren().add(player);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (double) (now - lastTimeFrame) / 50_000_000;

                player.getState().onUpdate(deltaTime, root);

                player.getCollider().setCollider(player.getPosX());

                lastTimeFrame = now;

                if(InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
                    enemySpawner();
                    System.out.println(enemyList);
                }
            }
        };

        timer.start();

        stage.setScene(scene);
        stage.setTitle("Offline Game");

        File file = new File("resources/game/soundFX/backsound.wav");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }

    private void setupBackground() {
        for (int i = 0; i < ScreenManager.SCREEN_WIDTH; i += 32) {
            for (int j = 0; j < ScreenManager.SCREEN_HEIGHT; j += 32) {
                Image groundSprite = groundSprites.get(new Random().nextInt(groundSprites.size()));
                ImageView tile = new ImageView(groundSprite);
                tile.setScaleX(2);
                tile.setScaleY(2);

                tile.setX(i);
                tile.setY(j);

                root.getChildren().add(tile);
            }
        }
    }

    private void enemySpawner() {
        Random random = new Random();
        double randomX = random.nextDouble() * ScreenManager.SCREEN_WIDTH;
        double randomY = random.nextDouble() * ScreenManager.SCREEN_HEIGHT;
        Enemy enemy = new Enemy(root, randomX, randomY, player, "soldier");
        enemyList.add(enemy);
    }

}
