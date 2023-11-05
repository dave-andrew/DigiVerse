package game.bullet;

import game.Bullet;
import helper.ScreenManager;

public class BulletMoveState extends BulletBaseState {
    public BulletMoveState(Bullet bullet) {
        super(bullet);
    }

    @Override
    public void onEnterState() {
    }

    @Override
    public void onUpdate(double deltaTime, String direction) {
        if (bullet.getPosX() > ScreenManager.SCREEN_WIDTH || bullet.getPosX() < 0 ||
                bullet.getPosY() > ScreenManager.SCREEN_HEIGHT || bullet.getPosY() < 0) {
            bullet.changeState(bullet.stopState);
            return;
        }

        double velX = 0;
        double velY = 0;

        double diagonalSpeed = bullet.getSpeed() / Math.sqrt(2);

        switch (direction) {
            case "right":
                velX = bullet.getSpeed();
                break;
            case "left":
                velX = -bullet.getSpeed();
                break;
            case "up":
                velY = -bullet.getSpeed();
                break;
            case "down":
                velY = bullet.getSpeed();
                break;
            case "up-right":
                velX = diagonalSpeed;
                velY = -diagonalSpeed;
                break;
            case "up-left":
                velX = -diagonalSpeed;
                velY = -diagonalSpeed;
                break;
            case "down-right":
                velX = diagonalSpeed;
                velY = diagonalSpeed;
                break;
            case "down-left":
                velX = -diagonalSpeed;
                velY = diagonalSpeed;
                break;
        }

        bullet.setPosX(bullet.getPosX() + velX * deltaTime);
        bullet.setPosY(bullet.getPosY() + velY * deltaTime);

        bullet.setX(bullet.getPosX());
        bullet.setY(bullet.getPosY());
    }
}