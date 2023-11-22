package game.player;

import game.Bullet;
import game.Player;
import game.bullet.BulletStopState;
import helper.BulletManager;
import helper.InputManager;
import helper.ScreenManager;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class PlayerBaseState {

    protected Player player;

    protected BulletManager bulletManager = BulletManager.getInstance();

    public PlayerBaseState(Player player) {
        this.player = player;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(double deltaTime, Pane root);

    public abstract void spriteManager(double velocityX, double velocityY, int frame);

    public void walk(double deltaTime, int frame) {
        double speed = player.getSpeed();
        double velocityX = 0;
        double velocityY = 0;

        if (InputManager.getPressedKeys().contains(KeyCode.D)) {
            velocityX += speed;
        }
        if (InputManager.getPressedKeys().contains(KeyCode.A)) {
            velocityX -= speed;
        }

        if (InputManager.getPressedKeys().contains(KeyCode.W)) {
            velocityY -= speed;
        }
        if (InputManager.getPressedKeys().contains(KeyCode.S)) {
            velocityY += speed;
        }

        if (velocityX != 0 && velocityY != 0) {
            double length = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            velocityX = (velocityX / length) * speed;
            velocityY = (velocityY / length) * speed;
        }

        spriteManager(velocityX, velocityY, frame);

        velocityX = checkXCollide(velocityX, deltaTime);
        velocityY = checkYCollide(velocityY, deltaTime);

        player.setPosX(player.getPosX() + velocityX * deltaTime);
        player.setPosY(player.getPosY() + velocityY * deltaTime);


        player.setX(player.getPosX());
        player.setY(player.getPosY());

        player.setImage(player.getSprite());
    }

    private double checkXCollide(double velocityX, double deltaTime) {
        if (player.getPosX() - velocityX * deltaTime <= 32 &&
                velocityX < 0) {
            velocityX = 0;

        }

        if(player.getPosX() + velocityX * deltaTime >= ScreenManager.SCREEN_WIDTH - 32 &&
                velocityX > 0) {
            velocityX = 0;
        }

        return velocityX;
    }

    private double checkYCollide(double velocityY, double deltaTime) {
        if (player.getPosY() - velocityY * deltaTime <= 32 &&
                velocityY < 0) {
            velocityY = 0;
        }

        if(player.getPosY() + velocityY * deltaTime >= ScreenManager.SCREEN_HEIGHT - 32 &&
                velocityY > 0) {
            velocityY = 0;
        }

        return velocityY;
    }

}
