package net.slc.dv.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import net.slc.dv.enums.PowerUp;
import net.slc.dv.game.player.*;
import net.slc.dv.helper.Collider;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.helper.ScreenManager;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Player extends ImageView {

    private static Player instance;

    @Getter
    private int lives;
    @Getter
    private double speed;
    @Getter
    private double shootcd;
    private double baseShootcd;
    @Getter
    private int score;

    @Getter
    private final Collider collider;

    //    Player Movement
    @Getter
    private double velocityX = 0.0;
    @Getter
    private double velocityY = 0.0;

    @Getter
    private double posX = ScreenManager.SCREEN_WIDTH / 2;
    @Getter
    private double posY = ScreenManager.SCREEN_HEIGHT / 2;

//    Player Sprites
    @Getter
    private final ArrayList<Image> leftSprites;
    @Getter
    private final ArrayList<Image> rightSprites;
    @Getter
    private final ArrayList<Image> upSprites;
    @Getter
    private final ArrayList<Image> downSprites;
    @Getter
    private final ArrayList<Image> diedSprites;


//    Player States
    private PlayerBaseState currentState;

    @Getter
    private final PlayerStandState standState;
    @Getter
    private final PlayerWalkState walkState;
    @Getter
    private final PlayerShootState shootState;
    @Getter
    private final PlayerDeadState deadState;
    @Getter
    private final PlayerRespawnState respawnState;
    @Getter
    private final PlayerNoLiveState noLiveState;

    @Getter
    private Image sprite;

    @Getter
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

        this.collider = new Collider(posX, posY);

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

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public void setShootcd(double shootcd) {
        this.shootcd = shootcd;
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

    public void setPowerUpTime(ConcurrentHashMap<PowerUp, Double> powerUpTime) {
        this.powerUpTime = powerUpTime;
    }


}

