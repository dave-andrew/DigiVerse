package game.dropitem.state;

import game.Enemy;
import game.Player;
import game.dropitem.DropItem;
import helper.ItemManager;
import view.OfflineGame;

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
