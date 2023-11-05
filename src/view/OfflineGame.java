package view;

import game.Bullet;
import game.Enemy;
import game.Player;
import helper.InputManager;
import helper.ScreenManager;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.input.KeyCode;

public class OfflineGame {

    private Player player;
    Pane root;
    private InputManager inputManager;

    private ArrayList<Enemy> enemyList = new ArrayList<>();

    private long lastTimeFrame = 0;
    public OfflineGame(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);

        inputManager = InputManager.getInstance(scene);

        player = Player.getInstance();
        player.setX(player.getPosX());
        player.setY(player.getPosY());

        root.getChildren().add(player);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (double) (now - lastTimeFrame) / 50_000_000;

                player.getState().onUpdate(deltaTime, root);

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


    }

    private void enemySpawner() {
        Random random = new Random();
        double randomX = random.nextDouble() * ScreenManager.SCREEN_WIDTH;
        double randomY = random.nextDouble() * ScreenManager.SCREEN_HEIGHT;
        Enemy enemy = new Enemy(root, randomX, randomY, player, "soldier");
        enemyList.add(enemy);
    }

}
