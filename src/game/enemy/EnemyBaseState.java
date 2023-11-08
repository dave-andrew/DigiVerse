package game.enemy;

import game.Enemy;
import helper.ItemManager;
import view.OfflineGame;

public abstract class EnemyBaseState {

    protected Enemy enemy;
    protected final ItemManager itemManager = ItemManager.getInstance();

    public EnemyBaseState(Enemy enemy) {
        this.enemy = enemy;
    }

    public abstract void onEnterState();
    public abstract void onUpdate(double deltaTime, OfflineGame game);

}
