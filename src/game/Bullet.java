package game;

import game.bullet.BulletBaseState;
import game.bullet.BulletMoveState;
import game.bullet.BulletStopState;
import helper.Collider;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Bullet extends ImageView {

    private double posX;
    private double posY;

    private int direction;

    private int speed;

    private Image sprite;

//    Bullet States
    private BulletBaseState currentState;
    public BulletMoveState moveState;
    public BulletStopState stopState;

//    private long lastTimeFrame = 0;

    private Collider collider;

    public Bullet(Pane root, double posX, double posY, int direction) {
        this.speed = 20;
        this.posX = posX;
        this.posY = posY;

        this.direction = direction;

        this.moveState = new BulletMoveState(this);
        this.stopState = new BulletStopState(this);

        this.currentState = moveState;

        this.sprite = new Image("file:resources/game/bullet.png");
        this.setImage(sprite);

        this.collider = new Collider(posX, posY, sprite.getWidth());

        root.getChildren().add(this);
        this.setX(posX);
        this.setY(posY);
    }

    public void changeState(BulletBaseState state) {
        this.currentState = state;
    }

    public BulletBaseState getState() {
        return currentState;
    }

    public void setState(BulletBaseState currentState) {
        this.currentState = currentState;
    }

    public int getSpeed() {
        return speed;
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

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    public int getDirection() {
        return this.direction;
    }
}
