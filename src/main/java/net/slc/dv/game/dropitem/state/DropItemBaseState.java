package net.slc.dv.game.dropitem.state;

import net.slc.dv.game.Enemy;
import net.slc.dv.game.Player;
import net.slc.dv.game.dropitem.DropItem;
import net.slc.dv.helper.ItemManager;

public abstract class DropItemBaseState {

    protected final Player player = Player.getInstance();
    protected final ItemManager itemManager = ItemManager.getInstance();
    protected Enemy enemy;
    protected DropItem dropItem;

    public DropItemBaseState(Enemy enemy, DropItem dropItem) {
        this.enemy = enemy;
        this.dropItem = dropItem;
    }

    public abstract void onEnterState();

}
