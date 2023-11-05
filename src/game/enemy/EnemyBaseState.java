package game.enemy;

import game.Enemy;

public abstract class EnemyBaseState {

    protected Enemy enemy;

    public EnemyBaseState(Enemy enemy) {
        this.enemy = enemy;
    }

    public abstract void onEnterState();
    public abstract void onUpdate(double deltaTime);

}
