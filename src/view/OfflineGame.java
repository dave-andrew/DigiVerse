package view;

import com.sun.media.jfxmedia.MediaError;
import game.Bullet;
import game.Enemy;
import game.Player;
import game.dropitem.DropItem;
import game.enemy.EnemyDeadState;
import game.enemy.EnemyDespawnState;
import game.gamestate.*;
import game.player.PlayerNoLiveState;
import game.player.PlayerStandState;
import helper.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class OfflineGame {

    private Stage stage;

    private AnimationTimer timer;

    private Player player;
    private Pane root;
    private Scene scene;
    private InputManager inputManager;
    private final ArrayList<Enemy> enemyList = new ArrayList<>();
    private ArrayList<Image> groundSprites;
    private long lastTimeFrame = 0;
    private boolean deadPause = false;
    private final BulletManager bulletManager = BulletManager.getInstance();
    private final ItemManager itemManager = ItemManager.getInstance();

//  Game State
    private GameBaseState currentState;
    public GameStartState startState;
    public GamePlayState playState;
    public GamePauseState pauseState;
    public GameOverState overState;

    public OfflineGame(Stage stage) {
        this.stage = stage;
        this.root = new Pane();

        this.scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        System.out.println("REEEEEEEEE" + this.scene);
        player = Player.getInstance();
        player.setX(ScreenManager.SCREEN_WIDTH / 2);
        player.setY(ScreenManager.SCREEN_HEIGHT / 2);

        groundSprites = ImageManager.importGroundSprites("tile");

        this.startState = new GameStartState(this);
        this.playState = new GamePlayState(this);
        this.pauseState = new GamePauseState(this);
        this.overState = new GameOverState(this);

        this.inputManager = InputManager.getInstance(this.scene);

        initialize(stage);
        setupGameLoop();
        setupAudio();

        this.currentState = this.startState;
        this.currentState.onEnterState();

        stage.setScene(scene);
        stage.setTitle("DigiVerse - Prairie King");
    }

    private void initialize(Stage stage) {

        setupBackground();

        setUpGui();
    }

    public void reinitialize() {
        setupBackground();

        setUpGui();
    }

    private boolean isPaused = false;
    private MediaPlayer mediaPlayer;

    private void setupGameLoop() {
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(currentState instanceof GamePauseState) {
                    isPaused = true;
                } else {
                    isPaused = false;
                }
                currentState.onUpdate(now);
            }
        };
        this.timer.start();
    }

    private void setupAudio() {
        File file = new File("resources/game/soundFX/backsound.wav");
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }

    public void updateGame(long now) {
        isPaused = currentState instanceof GamePauseState;

        handleInput();

        double deltaTime = (double) (now - lastTimeFrame) / 50_000_000;

        if (!isPaused) {
            clearPane();

            playerUpdate(deltaTime);
            updateBullets(deltaTime);
            checkCollisions(deltaTime);
            checkItemCollision();

            root.getChildren().add(player);

            setUpGui();
        }
        lastTimeFrame = now;
    }

    private boolean escapeKeyPressed = false;

    private void handleInput() {
        if (InputManager.getPressedKeys().contains(KeyCode.ESCAPE)) {
            if (!escapeKeyPressed) {
                escapeKeyPressed = true;
                togglePauseState();
            }
        } else {
            escapeKeyPressed = false;
        }
    }

    private void togglePauseState() {
        if (isPaused) {
            isPaused = false;
            changeState(playState);
        } else {
            isPaused = true;
            changeState(pauseState);
        }
    }

    private ImageView score;
    private Label scoreText;
    private ImageView health;
    private Label healthText;

    public void setUpGui() {
        score = new ImageView(new Image("file:resources/game/items/coin1.png"));
        score.setScaleX(2);
        score.setScaleY(2);
        score.setX(10);
        score.setY(10);
        root.getChildren().add(score);

        scoreText = new Label();
        scoreText.setText(Integer.toString(player.getScore()));
        scoreText.setScaleX(2);
        scoreText.setScaleY(2);
        scoreText.setLayoutX(50);
        scoreText.setLayoutY(10);
        root.getChildren().add(scoreText);

        health = new ImageView(ImageManager.importGUI("health-icon"));
        health.setScaleX(2);
        health.setScaleY(2);
        health.setX(10);
        health.setY(50);
        root.getChildren().add(health);

        healthText = new Label();
        if(player.getLives() < 0) {
            this.mediaPlayer.stop();
            healthText.setText("0");
        } else {
            healthText.setText(Integer.toString(player.getLives()));
        }
        healthText.setScaleX(2);
        healthText.setScaleY(2);
        healthText.setLayoutX(50);
        healthText.setLayoutY(50);
        root.getChildren().add(healthText);
    }

    public void clearPane() {
        root.getChildren().remove(player);

        root.getChildren().remove(score);
        root.getChildren().remove(scoreText);
        root.getChildren().remove(health);
        root.getChildren().remove(healthText);
    }

    private void playerUpdate(double deltaTime) {
        if(!isPaused) {
            player.getState().onUpdate(deltaTime, root);
            player.getCollider().setCollider(player.getPosX(), player.getPosY());
            if (InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
                enemySpawner();
            }
            if (player.getState() instanceof PlayerStandState) {
                deadPause = false;
            }
        }
    }

    private void updateBullets(double deltaTime) {
        if(!isPaused) {
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
    }

    private void checkCollisions(double deltaTime) {
        if (!deadPause && !isPaused) {
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
                enemy.getState().onUpdate(deltaTime, this);
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

    private void checkItemCollision() {
        List<DropItem> itemListCopy = new ArrayList<>(itemManager.getItemList());

        for (DropItem dropItem : itemListCopy) {
            dropItem.getState().onEnterState();
        }

//        System.out.println(player.getScore());
    }

    public void cleanUp() {
        root.getChildren().clear();

        player.setLives(3);
        player.setScore(0);
        player.changeState(player.respawnState);

        mediaPlayer.stop();
        mediaPlayer.dispose();

        root.getChildren().removeAll(enemyList);
        root.getChildren().removeAll(bulletManager.getBulletList());

        enemyList.clear();
        bulletManager.getBulletList().clear();
        itemManager.getItemList().clear();

        this.timer.stop();

        reinitialize();
        setupGameLoop();
        setupAudio();
    }

    public Scene getScene() {
        return scene;
    }

    public Pane getRoot() {
        return root;
    }

    public GameBaseState getState() {
        return currentState;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void changeState(GameBaseState newState) {
        this.currentState = newState;
        this.currentState.onEnterState();
    }
}
