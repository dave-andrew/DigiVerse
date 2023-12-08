package net.slc.dv.view.offlinegame;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.StackPaneBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.enums.PowerUp;
import net.slc.dv.game.Bullet;
import net.slc.dv.game.Enemy;
import net.slc.dv.game.Player;
import net.slc.dv.game.dropitem.DropItem;
import net.slc.dv.game.enemy.EnemyDeadState;
import net.slc.dv.game.enemy.EnemyDespawnState;
import net.slc.dv.game.gamestate.*;
import net.slc.dv.game.player.PlayerDeadState;
import net.slc.dv.game.player.PlayerRespawnState;
import net.slc.dv.game.player.PlayerStandState;
import net.slc.dv.helper.*;
import javafx.animation.AnimationTimer;
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

public class OfflineGameView {

    @Getter
    private final Player player;

    // Managers
    private final BulletManager bulletManager = BulletManager.getInstance();
    private final ItemManager itemManager = ItemManager.getInstance();

    //  Game State
    private GameBaseState currentState;

    @Getter
    private final GamePlayState playState;
    private final GamePauseState pauseState;
    @Getter
    private final GameOverState overState;
    private final GameLevelUpState gameLevelUpState;

    //    Game Layout
    @Getter
    private final Pane root;
    @Getter
    private final Scene scene;
    @Getter
    private final VBox pauseMenu;
    @Getter
    private final VBox settingMenu;
    @Getter
    private final Label fpsLabel;
    @Getter
    private final Label timerLabel;
    @Getter
    private StackPane playerStatPane;

    //  Entity Tracker
    @Getter
    private final ArrayList<Enemy> enemyList = new ArrayList<>();
    private final ArrayList<ArrayList<Image>> groundSprites;
    private final ArrayList<String> enemySprites;


    //    Game Attributes
    private int level = 0;
    @Getter
    private double enemySpawnRate = 0.01;
    @Getter
    private int baseEnemyHealth = 1;
    private long lastTimeFrame = 0;

    @Getter
    private AnimationTimer timer;
    @Getter
    private static MediaPlayer mediaPlayer;

    @Getter
    @Setter
    private boolean deadPause = false;
    private boolean isPaused = false;

    private Font font;

    public OfflineGameView(Stage stage) {
        this.root = new Pane();

        this.font = new Font(25);
        setupAudio();

        this.pauseMenu = new VBox(40);
        this.pauseMenu.setAlignment(Pos.CENTER);
        this.pauseMenu.setPrefSize(500, 600);

        this.settingMenu = new VBox(40);
        this.settingMenu.setAlignment(Pos.CENTER);
        this.settingMenu.setPrefSize(500, 400);

        this.scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        player = Player.getInstance();
        player.setX(ScreenManager.SCREEN_WIDTH / 2);
        player.setY(ScreenManager.SCREEN_HEIGHT / 2);

        this.fpsLabel = new Label("FPS: 0");
        this.fpsLabel.setFont(font);

        this.fpsLabel.setPrefWidth(150);
        this.fpsLabel.setLayoutX(root.getWidth() - fpsLabel.getPrefWidth() - 10);
        this.fpsLabel.setLayoutY(10);

        this.timerLabel = new Label("Time: 90s");
        timerLabel.setFont(font);
        this.timerLabel.setPrefWidth(150);
        this.timerLabel.setLayoutX((root.getWidth() - timerLabel.getPrefWidth()) / 2);
        this.timerLabel.setLayoutY(10);
        this.timerLabel.setAlignment(Pos.CENTER);

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

        scene.getStylesheets().add("file:resources/game_theme.css");

        stage.setScene(scene);
        stage.setTitle("DigiVerse - Prairie King");
    }

    private void initialize() {
        setupBackground();
        setUpGui();
    }

    public void reinitialize() {
        root.getChildren().clear();
        setupBackground();
        setUpGui();
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

    @Getter
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
        System.out.println(isPaused + " " + deadPause);
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
        score.setFitWidth(24);
        score.setPreserveRatio(true);
        score.setX(10);
        score.setY(10);

        scoreText = new Label();
        scoreText.setText(Integer.toString(player.getScore()));
        scoreText.setScaleX(1.5);
        scoreText.setScaleY(1.5);
        scoreText.setLayoutX(50);
        scoreText.setLayoutY(10);

        HBox scoreContainer = HBoxBuilder.create()
                .addChildren(score, scoreText)
                .setAlignment(Pos.CENTER_LEFT)
                .setSpacing(34)
                .setPadding(10, 10, 10, 14)
                .setLayoutX(10)
                .setLayoutY(10)
                .build();

        health = new ImageView(ImageManager.importGUI("health-icon"));
        health.setStyle("-fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: #000000;");
        health.setFitWidth(32);
        health.setPreserveRatio(true);
        health.setX(10);
        health.setY(50);

        healthText = new Label();
        if (player.getLives() < 0) {
            this.mediaPlayer.stop();
            healthText.setText("0");
        } else {
            healthText.setText(Integer.toString(player.getLives()));
        }
        healthText.setScaleX(1.5);
        healthText.setScaleY(1.5);
        healthText.setLayoutX(50);
        healthText.setLayoutY(50);

        HBox healthContainer = HBoxBuilder.create()
                .addChildren(health, healthText)
                .setAlignment(Pos.CENTER_LEFT)
                .setSpacing(30)
                .setPadding(10)
                .setLayoutX(10)
                .setLayoutY(50)
                .build();

        VBox playerStatContainer = VBoxBuilder.create()
                .addChildren(healthContainer, scoreContainer)
                .setAlignment(Pos.CENTER_LEFT)
                .setPadding(0, 10, 0, 10)
                .build();

        ImageView playerStatBackground = new ImageView(ImageManager.importGUI("stat-bg"));

        playerStatBackground.setFitWidth(170);
        playerStatBackground.setFitHeight(130);

        playerStatBackground.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,1), 10, 0, 0, 0);");

        this.playerStatPane = new StackPane();
        playerStatPane.getChildren().addAll(playerStatBackground, playerStatContainer);
        playerStatPane.setLayoutX(5);
        playerStatPane.setLayoutY(5);
        playerStatPane.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().add(playerStatPane);

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

        root.getChildren().remove(playerStatPane);
        root.getChildren().remove(fpsLabel);
        root.getChildren().remove(timerLabel);
    }

    double respawnTimer = 0;
    boolean isRespawn = false;

    private void playerUpdate(double deltaTime) {
        if (isPaused) {
            return;
        }

        player.getState().onUpdate(deltaTime, root);
        player.getCollider().setCollider(player.getPosX(), player.getPosY());

        if(isRespawn){
            respawnTimer += deltaTime;
        }

        if(respawnTimer >= 25 && isRespawn) {
            isRespawn = false;
            respawnTimer = 0;
            deadPause = false;
        }

        if (player.getState() instanceof PlayerRespawnState) {
            isRespawn = true;
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

        if(Math.random() <= 0.01) {
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

//        this.timer.stop();
        mediaPlayer.stop();

        player.setLives(3);
        player.setScore(0);
        player.changeState(player.getRespawnState());


        root.getChildren().removeAll(enemyList);
        root.getChildren().removeAll(bulletManager.getBulletList());

        enemyList.clear();
        bulletManager.getBulletList().clear();
        itemManager.getItemList().clear();


        setupAudio();
        reinitialize();
        setupGameLoop();
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

    public GameBaseState getState() {
        return currentState;
    }

    public void changeState(GameBaseState newState) {
        this.currentState = newState;
        this.currentState.onEnterState();
    }

    public void setEnemySpawnRate(double enemySpawnRate) {
        this.enemySpawnRate = enemySpawnRate;
    }

    public void setBaseEnemyHealth(int baseEnemyHealth) {
        this.baseEnemyHealth = baseEnemyHealth;
    }

}
