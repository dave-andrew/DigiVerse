package net.slc.dv.game.dropitem.state;

import net.slc.dv.game.Enemy;
import net.slc.dv.game.dropitem.DropItem;

public class DropItemDespawnState extends DropItemBaseState {


    public DropItemDespawnState(Enemy enemy, DropItem dropItem) {
        super(enemy, dropItem);
    }

    @Override
    public void onEnterState() {
        enemy.getRoot().getChildren().remove(dropItem);
        itemManager.removeItem(dropItem);
    }
}
