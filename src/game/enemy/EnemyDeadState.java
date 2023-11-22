package game.enemy;

import game.Enemy;
import game.dropitem.*;
import helper.ItemManager;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import view.OfflineGame;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemyDeadState extends EnemyBaseState {

    private double lastTimeFrame = 0;
    private int frame = 0;

    private List<DropItem> dropItems = new ArrayList<>();
    private final ItemManager itemManager = ItemManager.getInstance();

    private MediaPlayer mediaPlayer;

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
    public void onUpdate(double deltaTime, OfflineGame game) {
        this.lastTimeFrame += deltaTime;

        if (frame == 1200) {
            enemy.changeState(enemy.despawnState);
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

    private Random random = new Random();

    public void createDropItems() {
        double[] dropItemWeights = {0.1, 0.1, 0.005, 0.005, 0.005, 0.005, 0.005, 3};

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

                if (newDropItem != null) {
                    dropItems.add(newDropItem);
                     itemManager.addDropItem(newDropItem);
                }
                break;
            }
        }
    }

}
