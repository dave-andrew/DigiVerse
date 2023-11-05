package game;

import game.bullet.BulletBaseState;
import game.bullet.BulletMoveState;
import game.bullet.BulletStopState;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bullet extends ImageView {

    private double posX;
    private double posY;

    private int speed;

    private Image sprite;

//    Bullet States
    private BulletBaseState currentState;
    public BulletMoveState moveState;
    public BulletStopState stopState;

    private long lastTimeFrame = 0;

    public Bullet(Pane root, double posX, double posY, String direction) {
        this.speed = 10;
        this.posX = posX;
        this.posY = posY;

        this.moveState = new BulletMoveState(this);
        this.stopState = new BulletStopState(this);

        this.currentState = moveState;

        this.sprite = new Image("file:resources/game/bullet.png");
        this.setImage(sprite);

        root.getChildren().add(this);
        this.setX(posX);
        this.setY(posY);

        AnimationTimer run = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTimeFrame == 0) {
                    lastTimeFrame = now;
                } else if(currentState instanceof BulletStopState) {
                    root.getChildren().remove(Bullet.this);
                    this.stop();
                }

                double deltaTime = (double) (now - lastTimeFrame) / 25_000_000;

                currentState.onUpdate(deltaTime, direction);

                lastTimeFrame = now;
            }
        };

        run.start();
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
}