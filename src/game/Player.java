package game;

import game.player.PlayerBaseState;
import game.player.PlayerStandState;
import game.player.PlayerWalkState;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Player extends ImageView {

    private static Player instance;

    private int lives;
    private double speed;

    //    Player Movement
    private double velocityX = 0.0;
    private double velocityY = 0.0;

    private double posX = 100;
    private double posY = 100;
//    Player Control
    private KeyCode lastdir = KeyCode.S;


//    Player States
    private PlayerBaseState currentState;
    public PlayerStandState standState;
    public PlayerWalkState walkState;

    private Image sprite;


//    Singleton design pattern
    public static Player getInstance() {
        if(instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private Player() {
        this.lives = 3;
        this.speed = 4;

        this.standState = new PlayerStandState(this);
        this.walkState = new PlayerWalkState(this);

        this.currentState = standState;

        this.sprite = new Image("file:resources/icons/user.png");
        this.setImage(this.sprite);
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
}
