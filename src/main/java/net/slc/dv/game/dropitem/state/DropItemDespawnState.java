package net.slc.dv.game.dropitem.state;

import net.slc.dv.game.Enemy;
import net.slc.dv.game.dropitem.DropItem;
import net.slc.dv.view.offlinegame.OfflineGameView;

public class DropItemDespawnState extends DropItemBaseState {


    public DropItemDespawnState(OfflineGameView game, Enemy enemy, DropItem dropItem) {
        super(game, enemy, dropItem);
    }

    @Override
    public void onEnterState() {
        enemy.getRoot().getChildren().remove(dropItem);
        itemManager.removeItem(dropItem);
    }
}
