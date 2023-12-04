package net.slc.dv.game.dropitem.state;

import net.slc.dv.enums.PowerUp;
import net.slc.dv.game.Enemy;
import net.slc.dv.game.dropitem.*;
import net.slc.dv.game.enemy.EnemyDeadState;
import net.slc.dv.game.enemy.EnemyDespawnState;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.slc.dv.view.offlinegame.OfflineGameView;

import java.io.File;

public class DropItemSpawnState extends DropItemBaseState {
    private final MediaPlayer coinSound;
    private final MediaPlayer lifeSound;
    private final MediaPlayer powerupSound;
    private final double effectTime = 200.0;

    public DropItemSpawnState(OfflineGameView game, Enemy enemy, DropItem dropItem) {
        super(game, enemy, dropItem);

        File moneySFX = new File("resources/game/soundFX/money.wav");
        this.coinSound = new MediaPlayer(new Media(moneySFX.toURI().toString()));

        File lifeSFX = new File("resources/game/soundFX/lifeadd.wav");
        this.lifeSound = new MediaPlayer(new Media(lifeSFX.toURI().toString()));

        File powerupSFX = new File("resources/game/soundFX/powerup.wav");
        this.powerupSound = new MediaPlayer(new Media(powerupSFX.toURI().toString()));

    }

    @Override
    public void onEnterState() {
        if (enemy.getState() instanceof EnemyDespawnState) {
            dropItem.changeState(dropItem.getDespawnState());
        }

        if (player.getCollider().collidesWith(dropItem.getCollider())) {
            if (dropItem instanceof Coin1) {
                this.coinSound.play();
                player.setScore(player.getScore() + ((Coin1) dropItem).getValue());
            } else if (dropItem instanceof Coin5) {
                this.coinSound.play();
                player.setScore(player.getScore() + ((Coin5) dropItem).getValue());
            } else if (dropItem instanceof Life) {
                this.lifeSound.play();
                player.setLives(player.getLives() + 1);
            } else if (dropItem instanceof QuickLoad) {
                this.powerupSound.play();
                player.getPowerUpTime().put(PowerUp.QUICKLOAD, effectTime);
            } else if (dropItem instanceof ThreeShot) {
                this.powerupSound.play();
                player.getPowerUpTime().put(PowerUp.THREESHOT, effectTime);
            } else if (dropItem instanceof Nuke) {
                game.getEnemyList().forEach(enemy -> {
                    if (!(enemy.getState() instanceof EnemyDeadState)) {
                        enemy.changeState(enemy.getDespawnState());
                    }
                });
            } else if (dropItem instanceof Cartwheel) {
                this.powerupSound.play();
                player.getPowerUpTime().put(PowerUp.CARTWHEEL, effectTime);
            }

            dropItem.changeState(dropItem.getDespawnState());
        }
    }
}
