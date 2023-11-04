package view;

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
import java.util.Random;

import javafx.scene.input.KeyCode;

public class OfflineGame {

    private Player player;
    Pane root;
    private InputManager inputManager;

    public OfflineGame(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);

        inputManager = InputManager.getInstance(scene);

        player = Player.getInstance();
        player.setX(player.getPosX());
        player.setY(player.getPosY());

        root.getChildren().add(player);

        stage.setScene(scene);
        stage.setTitle("Offline Game");
        stage.show();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.getState().onUpdate();
//                System.out.println(player.getState());
            }
        };
        timer.start();
    }
}
