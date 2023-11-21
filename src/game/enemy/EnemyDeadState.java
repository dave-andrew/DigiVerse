package game.enemy;

import game.Enemy;
import game.dropitem.Coin1;
import game.dropitem.Coin5;
import game.dropitem.DropItem;
import game.dropitem.Life;
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

    private void createDropItems() {
        Random random = new Random();

        int randomNumber = random.nextInt(10);

        if (randomNumber < 1) {
            DropItem newDropItem = new Coin5(enemy, enemy.getPosX(), enemy.getPosY());
            itemManager.addDropItem(newDropItem);
            dropItems.add(newDropItem);
        } else if (randomNumber < 3) {
            DropItem newDropItem = new Coin1(enemy, enemy.getPosX(), enemy.getPosY());
            itemManager.addDropItem(newDropItem);
            dropItems.add(newDropItem);
        } else if(randomNumber < 4) {
            DropItem newDropItem = new Life(enemy, enemy.getPosX(), enemy.getPosY());
            itemManager.addDropItem(newDropItem);
            dropItems.add(newDropItem);
        }
    }

}
