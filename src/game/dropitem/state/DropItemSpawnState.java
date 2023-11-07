package game.dropitem.state;

import game.Enemy;
import game.dropitem.Coin1;
import game.dropitem.Coin5;
import game.dropitem.DropItem;
import game.enemy.EnemyDespawnState;

public class DropItemSpawnState extends DropItemBaseState {

    public DropItemSpawnState(Enemy enemy, DropItem dropItem) {
        super(enemy, dropItem);
    }

    @Override
    public void onEnterState() {
        if(enemy.getState() instanceof EnemyDespawnState) {
            dropItem.changeState(dropItem.despawnState);
        }

        if(player.getCollider().collidesWith(dropItem.getCollider())) {
            if(dropItem instanceof Coin1) {
                player.setScore(player.getScore() + ((Coin1) dropItem).getValue());
            } else if(dropItem instanceof Coin5) {
                player.setScore(player.getScore() + ((Coin5) dropItem).getValue());
            }

            dropItem.changeState(dropItem.despawnState);
        }
    }
}
