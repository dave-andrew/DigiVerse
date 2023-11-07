package game;

import game.player.*;
import helper.Collider;
import helper.ImageManager;
import helper.ScreenManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Player extends ImageView {

    private static Player instance;

    private int lives;
    private double speed;
    private double shootcd;

    private Collider collider;

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
    public PlayerStandState standState;
    public PlayerWalkState walkState;
    public PlayerShootState shootState;
    public PlayerDeadState deadState;
    public PlayerRespawnState respawnState;

    private Image sprite;

    public static Player getInstance() {
        if(instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private Player() {
        this.lives = 3;
        this.speed = 10;
        this.shootcd = 5;

        this.collider = new Collider(posX, posY);

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

        this.currentState = this.deadState;

        this.sprite = new Image("file:resources/game/player/down-1.png");
        this.setImage(this.sprite);

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
}
