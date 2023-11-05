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

        if (direction.equals("right")) {
            velX = bullet.getSpeed();
        } else if (direction.equals("left")) {
            velX = -bullet.getSpeed();
        } else if (direction.equals("up")) {
            velY = -bullet.getSpeed();
        } else if (direction.equals("down")) {
            velY = bullet.getSpeed();
        } else if (direction.equals("up-right")) {
            velX = diagonalSpeed;
            velY = -diagonalSpeed;
        } else if (direction.equals("up-left")) {
            velX = -diagonalSpeed;
            velY = -diagonalSpeed;
        } else if (direction.equals("down-right")) {
            velX = diagonalSpeed;
            velY = diagonalSpeed;
        } else if (direction.equals("down-left")) {
            velX = -diagonalSpeed;
            velY = diagonalSpeed;
        }

        bullet.setPosX(bullet.getPosX() + velX * deltaTime);
        bullet.setPosY(bullet.getPosY() + velY * deltaTime);

        bullet.setX(bullet.getPosX());
        bullet.setY(bullet.getPosY());
    }
}