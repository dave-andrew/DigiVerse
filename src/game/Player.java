package game;

import enums.PowerUp;
import game.player.*;
import helper.Collider;
import helper.ImageManager;
import helper.InputManager;
import helper.ScreenManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends ImageView {

    private static Player instance;

    private int lives;
    private double speed;
    private double shootcd;
    private double baseShootcd;
    private int score;

    private final Collider collider;

    //    Player Movement
    private double velocityX = 0.0;
    private double velocityY = 0.0;

    private double posX = ScreenManager.SCREEN_WIDTH / 2;
    private double posY = ScreenManager.SCREEN_HEIGHT / 2;

//    Player Sprites
    private final ArrayList<Image> leftSprites;
    private final ArrayList<Image> rightSprites;
    private final ArrayList<Image> upSprites;
    private final ArrayList<Image> downSprites;
    private final ArrayList<Image> diedSprites;


//    Player States
    private PlayerBaseState currentState;

    private final PlayerStandState standState;
    private final PlayerWalkState walkState;
    private final PlayerShootState shootState;
    private final PlayerDeadState deadState;
    private final PlayerRespawnState respawnState;
    private final PlayerNoLiveState noLiveState;

    private Image sprite;

    private ConcurrentHashMap<PowerUp, Double> powerUpTime = new ConcurrentHashMap<>();

    public static Player getInstance() {
        if(instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private Player() {
        this.lives = 3;
        this.speed = 10;
        this.baseShootcd = 8;
        this.shootcd = this.baseShootcd;
        this.score = 0;

        this.leftSprites = ImageManager.importPlayerSprites("left");
        this.rightSprites = ImageManager.importPlayerSprites("right");
        this.upSprites = ImageManager.importPlayerSprites("up");
        this.downSprites = ImageManager.importPlayerSprites("down");
        this.diedSprites = ImageManager.importDeadSprites("died");

        this.standState = new PlayerStandState(this);
        this.walkState = new PlayerWalkState(this);
        this.shootState = new PlayerShootState(this);
        this.deadState = new PlayerDeadState(this);
        this.respawnState = new PlayerRespawnState(this);
        this.noLiveState = new PlayerNoLiveState(this);

        this.currentState = this.standState;

        this.sprite = new Image("file:resources/game/player/down-1.png");
        this.setImage(this.sprite);

        this.collider = new Collider(posX, posY, sprite.getWidth());

        this.setScaleX(2);
        this.setScaleY(2);
    }

    public void changeState(PlayerBaseState playerState){
        this.currentState = playerState;
        this.currentState.onEnterState();
    }

    public PlayerBaseState getState() {
        return this.currentState;
    }
    public void setState(PlayerBaseState state) {
        this.currentState = state;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public ArrayList<Image> getLeftSprites() {
        return leftSprites;
    }

    public ArrayList<Image> getRightSprites() {
        return rightSprites;
    }

    public ArrayList<Image> getUpSprites() {
        return upSprites;
    }

    public ArrayList<Image> getDownSprites() {
        return downSprites;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public ArrayList<Image> getDiedSprites() {
        return diedSprites;
    }

    public double getShootcd() {
        return shootcd;
    }

    public void setShootcd(double shootcd) {
        this.shootcd = shootcd;
    }

    public Collider getCollider() {
        return collider;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public double getBaseShoodcd() {
        return this.baseShootcd;
    }
    public void setBaseShootcd(double baseShootcd) {
        this.baseShootcd = baseShootcd;
    }

    public ConcurrentHashMap<PowerUp, Double> getPowerUpTime() {
        return powerUpTime;
    }

    public void setPowerUpTime(ConcurrentHashMap<PowerUp, Double> powerUpTime) {
        this.powerUpTime = powerUpTime;
    }


    public PlayerStandState getStandState() {
        return standState;
    }

    public PlayerWalkState getWalkState() {
        return walkState;
    }

    public PlayerShootState getShootState() {
        return shootState;
    }

    public PlayerDeadState getDeadState() {
        return deadState;
    }

    public PlayerRespawnState getRespawnState() {
        return respawnState;
    }

    public PlayerNoLiveState getNoLiveState() {
        return noLiveState;
    }
}

