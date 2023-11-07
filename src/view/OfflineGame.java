package view;

import game.Bullet;
import game.Enemy;
import game.Player;
import game.dropitem.Coin1;
import game.dropitem.Coin5;
import game.dropitem.DropItem;
import game.enemy.EnemyDeadState;
import game.enemy.EnemyDespawnState;
import game.player.PlayerRespawnState;
import game.player.PlayerStandState;
import helper.*;
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
import java.util.Iterator;
import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class OfflineGame {

    private Player player;
    private Pane root;
    private InputManager inputManager;
    private ArrayList<Enemy> enemyList = new ArrayList<>();
    private ArrayList<Image> groundSprites;
    private long lastTimeFrame = 0;
    private boolean deadPause = false;
    private final BulletManager bulletManager = BulletManager.getInstance();
    private final ItemManager itemManager = ItemManager.getInstance();

    public OfflineGame(Stage stage) {
        initialize(stage);
        setupGameLoop();
        setupAudio();
    }

    private void initialize(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        inputManager = InputManager.getInstance(scene);
        player = Player.getInstance();
        player.setX(player.getPosX());
        player.setY(player.getPosY());
        groundSprites = ImageManager.importGroundSprites("tile");
        setupBackground();
        stage.setScene(scene);
        stage.setTitle("Offline Game");
    }

    private void setupGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame(now);
            }
        };
        timer.start();
    }

    private void setupAudio() {
        File file = new File("resources/game/soundFX/backsound.wav");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    private void updateGame(long now) {
        root.getChildren().remove(player);
        double deltaTime = (double) (now - lastTimeFrame) / 50_000_000;
        playerUpdate(deltaTime);
        updateBullets(deltaTime);
        checkCollisions(deltaTime);
        checkItemCollision(deltaTime);
        lastTimeFrame = now;
        root.getChildren().add(player);
    }

    private void playerUpdate(double deltaTime) {
        player.getState().onUpdate(deltaTime, root);
        player.getCollider().setCollider(player.getPosX(), player.getPosY());
        if (InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
            enemySpawner();
        }
        if (player.getState() instanceof PlayerStandState) {
            deadPause = false;
        }
    }

    private void updateBullets(double deltaTime) {
        Iterator<Bullet> bulletIterator = bulletManager.getBulletList().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.getPosX() < 0 || bullet.getPosX() > ScreenManager.SCREEN_WIDTH ||
                    bullet.getPosY() < 0 || bullet.getPosY() > ScreenManager.SCREEN_HEIGHT) {
                bullet.changeState(bullet.stopState);
                root.getChildren().remove(bullet);
                bulletIterator.remove();
            }
            bullet.getState().onUpdate(deltaTime, bullet.getDirection());
            bullet.getCollider().setCollider(bullet.getPosX(), bullet.getPosY());
        }
    }

    private void checkCollisions(double deltaTime) {
        if (!deadPause) {
            Iterator<Enemy> enemyIterator = enemyList.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if (enemy.getState() instanceof EnemyDespawnState) {
                    root.getChildren().remove(enemy);
                    enemyIterator.remove();
                    continue;
                }
                Iterator<Bullet> bulletIterator = bulletManager.getBulletList().iterator();
                while (bulletIterator.hasNext()) {
                    Bullet bullet = bulletIterator.next();
                    if (bullet.getCollider().collidesWith(enemy.getCollider()) && !(enemy.getState() instanceof EnemyDeadState)) {
                        bullet.changeState(bullet.stopState);
                        root.getChildren().remove(bullet);
                        enemy.changeState(enemy.deadState);
                        bulletIterator.remove();
                    } else if (bullet.getPosX() < 0 || bullet.getPosX() > ScreenManager.SCREEN_WIDTH ||
                            bullet.getPosY() < 0 || bullet.getPosY() > ScreenManager.SCREEN_HEIGHT) {
                        bullet.changeState(bullet.stopState);
                        root.getChildren().remove(bullet);
                        bulletIterator.remove();
                    }
                }
                if (enemy.getCollider().collidesWith(player.getCollider()) && !(enemy.getState() instanceof EnemyDeadState)) {
                    deadPause = true;
                    player.changeState(player.deadState);
                }
                enemy.getState().onUpdate(deltaTime);
                enemy.getCollider().setCollider(enemy.getPosX(), enemy.getPosY());
            }
        }
    }

    private void setupBackground() {
        for (int i = 0; i < ScreenManager.SCREEN_WIDTH + 64; i += 32) {
            for (int j = 0; j < ScreenManager.SCREEN_HEIGHT + 64; j += 32) {
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

    private void checkItemCollision(double deltaTime) {
        Iterator<DropItem> dropItemIterator = itemManager.getItemList().iterator();
        while (dropItemIterator.hasNext()) {
            DropItem dropItem = dropItemIterator.next();
            if (dropItem.getCollider().collidesWith(player.getCollider())) {
                root.getChildren().remove(dropItem);
                dropItemIterator.remove();
                if (dropItem instanceof Coin1) {
                    player.setScore(player.getScore() + 1);
                } else if (dropItem instanceof Coin5) {
                    player.setScore(player.getScore() + 5);
                }
            }
        }
        System.out.println(player.getScore());
    }
}
