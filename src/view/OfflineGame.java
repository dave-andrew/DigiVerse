package view;

import game.Bullet;
import game.Enemy;
import game.Player;
import game.enemy.EnemyDeadState;
import game.enemy.EnemyDespawnState;
import game.player.PlayerRespawnState;
import game.player.PlayerStandState;
import helper.BulletManager;
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

    private ArrayList<Image>  groundSprites;

    private long lastTimeFrame = 0;
    private boolean deadPause = false;

    private BulletManager bulletManager = BulletManager.getInstance();
    public OfflineGame(Stage stage) {

        root = new Pane();
        Scene scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);

        inputManager = InputManager.getInstance(scene);

        player = Player.getInstance();
        player.setX(player.getPosX());
        player.setY(player.getPosY());

        groundSprites = ImageManager.importGroundSprites("tile");
        setupBackground();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                root.getChildren().remove(player);
                double deltaTime = (double) (now - lastTimeFrame) / 50_000_000;

                player.getState().onUpdate(deltaTime, root);
                player.getCollider().setCollider(player.getPosX(), player.getPosY());

                lastTimeFrame = now;

                if(InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
                    enemySpawner();
                }

                if(player.getState() instanceof PlayerStandState) {
                    deadPause = false;
                }

                ArrayList<Bullet> bulletList = new ArrayList<>();

                for(Bullet bullet : bulletManager.getBulletList()) {

                    if(bullet.getPosX() < 0 || bullet.getPosX() > ScreenManager.SCREEN_WIDTH ||
                            bullet.getPosY() < 0 || bullet.getPosY() > ScreenManager.SCREEN_HEIGHT) {
                        bullet.changeState(bullet.stopState);
                        root.getChildren().remove(bullet);

                        bulletList.add(bullet);
                    }

                    bullet.getState().onUpdate(deltaTime, bullet.getDirection());
                    bullet.getCollider().setCollider(bullet.getPosX(), bullet.getPosY());
                }

                if(!deadPause) {
                    for (Enemy enemy : enemyList) {

                        if(enemy.getState() instanceof EnemyDespawnState) {
                            root.getChildren().remove(enemy);
                            enemyList.remove(enemy);
                            continue;
                        }



                        for (Bullet bullet : bulletManager.getBulletList()) {
                            if(bullet.getCollider().collidesWith(enemy.getCollider()) && !(enemy.getState() instanceof EnemyDeadState)) {
                                bullet.changeState(bullet.stopState);
                                root.getChildren().remove(bullet);

                                enemy.changeState(enemy.deadState);
                                bulletList.add(bullet);
                            } else if(bullet.getPosX() < 0 || bullet.getPosX() > ScreenManager.SCREEN_WIDTH ||
                                    bullet.getPosY() < 0 || bullet.getPosY() > ScreenManager.SCREEN_HEIGHT) {
                                bullet.changeState(bullet.stopState);
                                root.getChildren().remove(bullet);

                                bulletList.add(bullet);
                            }
                        }

                        bulletManager.removeAllBullet(bulletList);

                        if(enemy.getCollider().collidesWith(player.getCollider()) && !(enemy.getState() instanceof EnemyDeadState)) {
                            deadPause = true;
                            player.changeState(player.deadState);
                        }

                        enemy.getState().onUpdate(deltaTime);
                        enemy.getCollider().setCollider(enemy.getPosX(), enemy.getPosY());

                    }
                }
                root.getChildren().add(player);
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
