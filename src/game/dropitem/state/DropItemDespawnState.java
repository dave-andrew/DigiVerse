package game.dropitem.state;

import game.Enemy;
import game.dropitem.DropItem;
import view.OfflineGame;

public class DropItemDespawnState extends DropItemBaseState {


    public DropItemDespawnState(OfflineGame game, Enemy enemy, DropItem dropItem) {
        super(game, enemy, dropItem);
    }

    @Override
    public void onEnterState() {
        enemy.getRoot().getChildren().remove(dropItem);
        itemManager.removeItem(dropItem);
    }
}
