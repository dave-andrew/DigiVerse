package net.slc.dv.game.bullet;

import net.slc.dv.game.Bullet;
import net.slc.dv.helper.ScreenManager;

public class BulletMoveState extends BulletBaseState {
    public BulletMoveState(Bullet bullet) {
        super(bullet);
    }

    @Override
    public void onEnterState() {
    }

    @Override
    public void onUpdate(double deltaTime, int direction) {
        if (bullet.getPosX() > ScreenManager.SCREEN_WIDTH || bullet.getPosX() < 0 ||
                bullet.getPosY() > ScreenManager.SCREEN_HEIGHT || bullet.getPosY() < 0) {
            bullet.changeState(bullet.getStopState());
            return;
        }

        double velX = 0;
        double velY = 0;

        double diagonalSpeed = bullet.getSpeed() / Math.sqrt(2);

        switch (direction) {
            case 90:
                velX = bullet.getSpeed();
                break;
            case -90:
                velX = -bullet.getSpeed();
                break;
            case 0:
                velY = -bullet.getSpeed();
                break;
            case 180:
                velY = bullet.getSpeed();
                break;
            case 45:
                velX = diagonalSpeed;
                velY = -diagonalSpeed;
                break;
            case -45:
                velX = -diagonalSpeed;
                velY = -diagonalSpeed;
                break;
            case 135:
                velX = diagonalSpeed;
                velY = diagonalSpeed;
                break;
            case -135:
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
