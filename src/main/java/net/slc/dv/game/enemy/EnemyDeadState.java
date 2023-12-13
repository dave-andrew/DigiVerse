package net.slc.dv.game.enemy;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.slc.dv.game.Enemy;
import net.slc.dv.game.dropitem.*;
import net.slc.dv.helper.ItemManager;
import net.slc.dv.view.offlinegame.OfflineGameView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyDeadState extends EnemyBaseState {

    private final List<DropItem> dropItems = new ArrayList<>();
    private final ItemManager itemManager = ItemManager.getInstance();
    private final MediaPlayer mediaPlayer;
    private final Random random = new Random();
    private double lastTimeFrame = 0;
    private int frame = 0;

    public EnemyDeadState(Enemy enemy) {
        super(enemy);

        File dieSFX = new File("resources/game/soundFX/enemy-dead.wav");
        this.mediaPlayer = new MediaPlayer(new Media(dieSFX.toURI().toString()));
    }

    @Override
    public void onEnterState() {
        this.lastTimeFrame = 0;
        this.frame = 0;
        this.mediaPlayer.play();
        createDropItems();
    }

    @Override
    public void onUpdate(double deltaTime, OfflineGameView game) {
        this.lastTimeFrame += deltaTime;

        if (frame == 1200) {
            enemy.changeState(enemy.getDespawnState());
        }

        if (lastTimeFrame > 2) {
            this.frame++;
            if (this.frame < 6) {
                this.lastTimeFrame = 0;
            }
        }

        if (frame < 5) {
            enemy.setSprite(enemy.getDiedSprites().get(frame));
            enemy.setImage(enemy.getSprite());
        } else {
            enemy.setImage(enemy.getDiedSprites().get(enemy.getDiedSprites().size() - 1));
        }
    }

    public void createDropItems() {
        double[] dropItemWeights = {0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05};

        double randomNumber = random.nextDouble();
        double cumulativeWeight = 0;

        for (int i = 0; i < dropItemWeights.length; i++) {
            cumulativeWeight += dropItemWeights[i];
            if (randomNumber < cumulativeWeight) {
                DropItem newDropItem = null;
                switch (i) {
                    case 0:
                        newDropItem = new Coin1(enemy, enemy.getPosX(), enemy.getPosY());
                        break;
                    case 1:
                        newDropItem = new Coin5(enemy, enemy.getPosX(), enemy.getPosY());
                        break;
                    case 2:
                        newDropItem = new Life(enemy, enemy.getPosX(), enemy.getPosY());
                        break;
                    case 3:
                        newDropItem = new QuickLoad(enemy, enemy.getPosX(), enemy.getPosY());
                        break;
                    case 4:
                        newDropItem = new ThreeShot(enemy, enemy.getPosX(), enemy.getPosY());
                        break;
                    case 5:
                        newDropItem = new Nuke(enemy, enemy.getPosX(), enemy.getPosY());
                        break;
                    case 6:
                        newDropItem = new Cartwheel(enemy, enemy.getPosX(), enemy.getPosY());
                        break;
                }

                dropItems.add(newDropItem);
                itemManager.addDropItem(newDropItem);
                break;
            }
        }
    }

}
