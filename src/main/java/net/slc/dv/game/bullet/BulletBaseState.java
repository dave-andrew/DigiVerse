package net.slc.dv.game.bullet;

import net.slc.dv.game.Bullet;

public abstract class BulletBaseState {

    protected Bullet bullet;

    public BulletBaseState(Bullet bullet) {
        this.bullet = bullet;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(double deltaTime, int direction);

}
