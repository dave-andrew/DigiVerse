package net.slc.dv.view;

import net.slc.dv.enums.PowerUp;
import net.slc.dv.game.Bullet;
import net.slc.dv.game.Enemy;
import net.slc.dv.game.Player;
import net.slc.dv.game.dropitem.DropItem;
import net.slc.dv.game.enemy.EnemyDeadState;
import net.slc.dv.game.enemy.EnemyDespawnState;
import net.slc.dv.game.gamestate.*;
import net.slc.dv.game.player.PlayerStandState;
import net.slc.dv.helper.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.*;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class OfflineGame {

    private final Player player;

    // Managers
    private final BulletManager bulletManager = BulletManager.getInstance();
    private final ItemManager itemManager = ItemManager.getInstance();

    //  Game State
    private GameBaseState currentState;

    private final GamePlayState playState;
    private final GamePauseState pauseState;
    private final GameOverState overState;
    private final GameLevelUpState gameLevelUpState;

    //    Game Layout
    private final Pane root;
    private final Scene scene;
    private final VBox pauseMenu;
    private final VBox settingMenu;
    private final Label fpsLabel;
    private final Label timerLabel;

    //  Entity Tracker
    private final ArrayList<Enemy> enemyList = new ArrayList<>();
    private final ArrayList<ArrayList<Image>> groundSprites;
    private final ArrayList<String> enemySprites;


    //    Game Attributes
    private int level = 0;
    private double enemySpawnRate = 0.01;
    private int baseEnemyHealth = 1;
    private long lastTimeFrame = 0;

    private AnimationTimer timer;
    private MediaPlayer mediaPlayer;

    private boolean deadPause = false;
    private boolean isPaused = false;

    public OfflineGame(Stage stage) {
        this.root = new Pane();
        setupAudio();

        this.pauseMenu = new VBox(40);
        this.pauseMenu.setAlignment(Pos.CENTER);
        this.pauseMenu.setPrefSize(500, 400);

        this.settingMenu = new VBox(40);
        this.settingMenu.setAlignment(Pos.CENTER);
        this.settingMenu.setPrefSize(500, 400);

        this.scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        player = Player.getInstance();
        player.setX(ScreenManager.SCREEN_WIDTH / 2);
        player.setY(ScreenManager.SCREEN_HEIGHT / 2);

        this.fpsLabel = new Label("FPS: 0");

        this.fpsLabel.setPrefWidth(100);
        this.fpsLabel.setLayoutX(root.getWidth() - fpsLabel.getPrefWidth() - 10);
        this.fpsLabel.setLayoutY(10);

        this.timerLabel = new Label();
        this.timerLabel.setPrefWidth(100);
        this.timerLabel.setLayoutX((root.getWidth() - timerLabel.getPrefWidth()) / 2);
        this.timerLabel.setLayoutY(10);

        this.groundSprites = new ArrayList<>();
        groundSprites.add(0, ImageManager.importGroundSprites("tile"));
        groundSprites.add(1, ImageManager.importGroundSprites("rock"));
        groundSprites.add(2, ImageManager.importGroundSprites("sand"));

        this.enemySprites = new ArrayList<>();
        enemySprites.add(0, "soldier");
        enemySprites.add(1, "mummy");
        enemySprites.add(2, "bug");


        GameStartState startState = new GameStartState(this);
        this.playState = new GamePlayState(this);
        this.pauseState = new GamePauseState(this);
        this.overState = new GameOverState(this);
        this.gameLevelUpState = new GameLevelUpState(this);

        initialize();
        setupGameLoop();

        InputManager.getInstance(this.scene);

        this.currentState = startState;
        this.currentState.onEnterState();

        scene.getStylesheets().add("file:resources/light_theme.css");

        stage.setScene(scene);
        stage.setTitle("DigiVerse - Prairie King");
    }

    private void initialize() {
        setupBackground();
        setUpGui();
    }

    public void reinitialize() {
        setupBackground();
        setUpGui();
    }

    public Label getFpsLabel() {
        return fpsLabel;
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    private void setupGameLoop() {
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (currentState instanceof GamePauseState) {
                    isPaused = true;
                    mediaPlayer.pause();
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

        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    private double TARGET_FPS = 144.0;
    private double TARGET_FRAME_TIME = 1.0 / TARGET_FPS * 20;

    public void setTargetFPS(double fps) {
        TARGET_FPS = fps;
        TARGET_FRAME_TIME = 1.0 / TARGET_FPS * 20;
    }

    public double getTargetFPS() {
        return TARGET_FPS;
    }

    private final int INITIAL_TIMER_VALUE = 90;

    public void updateGame(long now) {
        isPaused = currentState instanceof GamePauseState;

        handleInput();

        double deltaTime = (double) (now - lastTimeFrame) / 50_000_000;
        while (deltaTime < TARGET_FRAME_TIME) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            now = System.nanoTime();
            deltaTime = (double) (now - lastTimeFrame) / 50_000_000;
        }

        fpsLabel.setText("FPS: " + (int) (1 / deltaTime * 20));

        if (!isPaused) {
            clearPane();

            playerUpdate(deltaTime);
            updateBullets(deltaTime);
            checkCollisions(deltaTime);
            checkItemCollision();

            root.getChildren().add(player);
            setUpGui();

            if (Math.random() <= enemySpawnRate) {
                enemySpawner();
            }
        }

        lastTimeFrame = now;
    }

    private int batchTimer = INITIAL_TIMER_VALUE;
    private long lastUpdateTime = System.currentTimeMillis();

    public int getInitialTimer() {
        return INITIAL_TIMER_VALUE;
    }

    public void setBatchTimer(int batchTimer) {
        this.batchTimer = batchTimer;
    }

    private void updateTimer() {
        if (!isPaused && !deadPause) {
            long currentTime = System.currentTimeMillis();
            long elapsedTimeMillis = currentTime - lastUpdateTime;

            if (elapsedTimeMillis >= 1000) {
                lastUpdateTime = currentTime;
                batchTimer--;
                timerLabel.setText("Time: " + batchTimer + "s");

                if (batchTimer <= 0) {
                    batchTimer = INITIAL_TIMER_VALUE;
                    this.changeState(this.gameLevelUpState);
                    timerLabel.setText("Time: " + batchTimer + "s");
                }
            }
        }
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
            this.getRoot().getChildren().remove(this.getPauseMenu());
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
        if (player.getLives() < 0) {
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

        root.getChildren().add(fpsLabel);
        root.getChildren().add(timerLabel);

        updateTimer();
    }

    public void clearPane() {
        root.getChildren().remove(player);

        root.getChildren().remove(score);
        root.getChildren().remove(scoreText);
        root.getChildren().remove(health);
        root.getChildren().remove(healthText);

        root.getChildren().remove(fpsLabel);
        root.getChildren().remove(timerLabel);
    }

    private void playerUpdate(double deltaTime) {
        if (isPaused) {
            return;
        }

        player.getState().onUpdate(deltaTime, root);
        player.getCollider().setCollider(player.getPosX(), player.getPosY());

        if (player.getState() instanceof PlayerStandState) {
            deadPause = false;
        }

        checkPowerUps(deltaTime);
    }

    private void checkPowerUps(double deltaTime) {
        synchronized (player) {
            Map<PowerUp, Double> powerUpTimeMap = player.getPowerUpTime();
            List<PowerUp> powerUpsToRemove = new ArrayList<>();

            for (Map.Entry<PowerUp, Double> entry : powerUpTimeMap.entrySet()) {
                PowerUp powerUp = entry.getKey();
                double time = entry.getValue();

                if (time > 0) {
                    powerUpTimeMap.put(powerUp, time - deltaTime);
                } else {
                    powerUpsToRemove.add(powerUp);
                }
            }

            for (PowerUp powerUpToRemove : powerUpsToRemove) {
                player.getPowerUpTime().remove(powerUpToRemove);
            }
        }
    }

    private void updateBullets(double deltaTime) {
        if (isPaused) {
            return;
        }

        Iterator<Bullet> bulletIterator = bulletManager.getBulletList().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.getPosX() < 0 || bullet.getPosX() > ScreenManager.SCREEN_WIDTH ||
                    bullet.getPosY() < 0 || bullet.getPosY() > ScreenManager.SCREEN_HEIGHT) {
                bullet.changeState(bullet.getStopState());
                root.getChildren().remove(bullet);
                bulletIterator.remove();
            }
            bullet.getState().onUpdate(deltaTime, bullet.getDirection());
            bullet.getCollider().setCollider(bullet.getPosX(), bullet.getPosY());
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
                        bullet.changeState(bullet.getStopState());
                        root.getChildren().remove(bullet);
                        enemy.setHealth(enemy.getHealth() - 1);
                        bulletIterator.remove();
                    } else if (bullet.getPosX() < 0 || bullet.getPosX() > ScreenManager.SCREEN_WIDTH ||
                            bullet.getPosY() < 0 || bullet.getPosY() > ScreenManager.SCREEN_HEIGHT) {
                        bullet.changeState(bullet.getStopState());
                        root.getChildren().remove(bullet);
                        bulletIterator.remove();
                    }
                }
                if (enemy.getCollider().collidesWith(player.getCollider()) && !(enemy.getState() instanceof EnemyDeadState)) {
                    deadPause = true;
                    player.changeState(player.getDeadState());
                }
                enemy.getState().onUpdate(deltaTime, this);
                enemy.getCollider().setCollider(enemy.getPosX(), enemy.getPosY());
            }
        }
    }

    private void setupBackground() {
        ArrayList<Image> groundSprite = groundSprites.get(level);

        for (int i = 0; i < ScreenManager.SCREEN_WIDTH + 64; i += 32) {
            for (int j = 0; j < ScreenManager.SCREEN_HEIGHT + 64; j += 32) {
                Image ground = groundSprite.get(new Random().nextInt(groundSprite.size()));
                ImageView tile = new ImageView(ground);
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

        int spawnSide = random.nextInt(4);

        double randomX;
        double randomY;

        if (spawnSide == 0) {
            randomX = random.nextDouble() * ScreenManager.SCREEN_WIDTH;
            randomY = -32;
        } else if (spawnSide == 1) {
            randomX = 32 + ScreenManager.SCREEN_WIDTH;
            randomY = random.nextDouble() * ScreenManager.SCREEN_HEIGHT;
        } else if (spawnSide == 2) {
            randomX = random.nextDouble() * ScreenManager.SCREEN_WIDTH;
            randomY = 32 + ScreenManager.SCREEN_HEIGHT;
        } else {
            randomX = -32;
            randomY = random.nextDouble() * ScreenManager.SCREEN_HEIGHT;
        }

        String enemyType = enemySprites.get(random.nextInt(enemySprites.size()));

        if(Math.random() <= 0.5) {
            enemyType = "spider";
        }

        Enemy enemy = new Enemy(this, root, randomX, randomY, player, enemyType, baseEnemyHealth);
        enemyList.add(enemy);
    }

    private void checkItemCollision() {
        List<DropItem> itemListCopy = new ArrayList<>(itemManager.getItemList());

        for (DropItem dropItem : itemListCopy) {
            dropItem.getState().onEnterState();
        }
    }

    public void cleanUp() {
        root.getChildren().clear();

        player.setLives(3);
        player.setScore(0);
        player.changeState(player.getRespawnState());

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

    public void addLevel() {
        if(this.level == 2) {
            this.level = 0;
            return;
        }
        this.level++;
    }

    public void resetLevel() {
        this.level = 0;
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

    public VBox getPauseMenu() {
        return pauseMenu;
    }

    public VBox getSettingMenu() {
        return settingMenu;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public double getEnemySpawnRate() {
        return this.enemySpawnRate;
    }

    public void setEnemySpawnRate(double enemySpawnRate) {
        this.enemySpawnRate = enemySpawnRate;
    }

    public int getBaseEnemyHealth() {
        return baseEnemyHealth;
    }

    public void setBaseEnemyHealth(int baseEnemyHealth) {
        this.baseEnemyHealth = baseEnemyHealth;
    }

    public GameOverState getOverState() {
        return overState;
    }

    public GamePlayState getPlayState() {
        return playState;
    }

    public ArrayList<Enemy> getEnemyList() {
        return enemyList;
    }

}
