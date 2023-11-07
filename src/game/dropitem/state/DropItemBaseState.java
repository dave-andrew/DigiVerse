package game.dropitem.state;

import game.Enemy;
import game.Player;
import game.dropitem.DropItem;
import helper.ItemManager;

public abstract class DropItemBaseState {

    protected Enemy enemy;
    protected DropItem dropItem;
    protected final Player player = Player.getInstance();
    protected final ItemManager itemManager = ItemManager.getInstance();

    public DropItemBaseState(Enemy enemy, DropItem dropItem) {
        this.enemy = enemy;
        this.dropItem = dropItem;
    }

    public abstract void onEnterState();

}
