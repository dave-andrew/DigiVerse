package net.slc.dv.game.enemy;

import net.slc.dv.game.Enemy;
import net.slc.dv.view.OfflineGame;

public abstract class EnemyBaseState {

    protected Enemy enemy;

    public EnemyBaseState(Enemy enemy) {
        this.enemy = enemy;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(double deltaTime, OfflineGame game);

}
