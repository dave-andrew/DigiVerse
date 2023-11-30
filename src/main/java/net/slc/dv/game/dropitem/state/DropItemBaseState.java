package net.slc.dv.game.dropitem.state;

import net.slc.dv.game.Enemy;
import net.slc.dv.game.Player;
import net.slc.dv.game.dropitem.DropItem;
import net.slc.dv.helper.ItemManager;
import net.slc.dv.view.OfflineGame;

public abstract class DropItemBaseState {

    protected OfflineGame game;

    protected Enemy enemy;
    protected DropItem dropItem;
    protected final Player player = Player.getInstance();
    protected final ItemManager itemManager = ItemManager.getInstance();

    public DropItemBaseState(OfflineGame game, Enemy enemy, DropItem dropItem) {
        this.game = game;
        this.enemy = enemy;
        this.dropItem = dropItem;
    }

    public abstract void onEnterState();

}
