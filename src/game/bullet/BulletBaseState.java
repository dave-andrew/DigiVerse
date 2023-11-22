package game.bullet;

import game.Bullet;

public abstract class BulletBaseState {

    protected Bullet bullet;

    public BulletBaseState(Bullet bullet) {
        this.bullet = bullet;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(double deltaTime, int direction);

}
