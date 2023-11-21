package game.dropitem.state;

import game.Enemy;
import game.dropitem.*;
import game.enemy.EnemyDespawnState;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class DropItemSpawnState extends DropItemBaseState {
    private MediaPlayer mediaPlayer;

    public DropItemSpawnState(Enemy enemy, DropItem dropItem) {
        super(enemy, dropItem);

        File moneySFX = new File("resources/game/soundFX/money.wav");
        this.mediaPlayer = new MediaPlayer(new Media(moneySFX.toURI().toString()));
    }

    @Override
    public void onEnterState() {
        if(enemy.getState() instanceof EnemyDespawnState) {
            dropItem.changeState(dropItem.despawnState);
        }

        if(player.getCollider().collidesWith(dropItem.getCollider())) {
            this.mediaPlayer.play();
            if(dropItem instanceof Coin1) {
                player.setScore(player.getScore() + ((Coin1) dropItem).getValue());
            } else if(dropItem instanceof Coin5) {
                player.setScore(player.getScore() + ((Coin5) dropItem).getValue());
            } else if(dropItem instanceof Life) {
                player.setLives(player.getLives() + 1);
            } else if(dropItem instanceof QuickLoad) {

            }else if(dropItem instanceof ThreeShot) {

            } else if(dropItem instanceof Nuke) {

            } else if(dropItem instanceof Cartwheel) {

            }

            dropItem.changeState(dropItem.despawnState);
        }
    }
}
