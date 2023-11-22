package game.player;

import game.Bullet;
import game.Player;
import helper.InputManager;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class PlayerShootState extends PlayerBaseState {
    private int frame = 0;
    private double lastTimeFrame = 0;
    private double bulletCooldown;
    private double timeSinceLastBullet = 0;
    private boolean canSpawnBullet = true;
    private Pane root;

    public PlayerShootState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {
        this.bulletCooldown = player.getShootcd();
    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {
        this.root = root;

        timeSinceLastBullet += deltaTime;

        if (timeSinceLastBullet >= bulletCooldown) {
            canSpawnBullet = true;
        }

        this.lastTimeFrame += deltaTime;

        if (lastTimeFrame > 3) {
            this.frame++;
            this.lastTimeFrame = 0;
        }
        if (frame == 4) {
            this.frame = 0;
        }

        walk(deltaTime, frame);
    }

    @Override
    public void spriteManager(double velocityX, double velocityY, int frame) {
        if(InputManager.getPressedKeys().contains(KeyCode.UP) && InputManager.getPressedKeys().contains(KeyCode.RIGHT)){
            if (validateMove()) {
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getRightSprites().get(frame));
            }

            spawnBullet(root, 45);

        } else if(InputManager.getPressedKeys().contains(KeyCode.UP) && InputManager.getPressedKeys().contains(KeyCode.LEFT)){
            if (validateMove()) {
                player.setSprite(player.getLeftSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(frame));
            }

            spawnBullet(root, -45);

        } else if(InputManager.getPressedKeys().contains(KeyCode.DOWN) && InputManager.getPressedKeys().contains(KeyCode.RIGHT)){
            if (validateMove()) {
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getRightSprites().get(frame));
            }

            spawnBullet(root, 135);

        } else if(InputManager.getPressedKeys().contains(KeyCode.DOWN) && InputManager.getPressedKeys().contains(KeyCode.LEFT)) {
            if (validateMove()) {
                player.setSprite(player.getLeftSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(frame));
            }

            spawnBullet(root, -135);
        } else if (InputManager.getPressedKeys().contains(KeyCode.RIGHT)){
            if (validateMove()){
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getRightSprites().get(frame));
            }

            spawnBullet(root, 90);

        } else if (InputManager.getPressedKeys().contains(KeyCode.LEFT)){
            if (validateMove()){
                player.setSprite(player.getLeftSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(frame));
            }

            spawnBullet(root, -90);

        } else if (InputManager.getPressedKeys().contains(KeyCode.UP)){
            if (validateMove()) {
                player.setSprite(player.getUpSprites().get(0));
            } else {
                player.setSprite(player.getUpSprites().get(frame));
            }

            spawnBullet(root, 0);

        } else if (InputManager.getPressedKeys().contains(KeyCode.DOWN)) {
            if (validateMove()) {
                player.setSprite(player.getDownSprites().get(0));
            } else {
                player.setSprite(player.getDownSprites().get(frame));
            }

            spawnBullet(root, 180);

        } else if (InputManager.getPressedKeys().isEmpty()){
            player.changeState(player.standState);
            return;
        } else if (!InputManager.getPressedKeys().isEmpty()){
            player.changeState(player.walkState);
            return;
        }
    }

    private boolean validateMove() {
        return !(InputManager.getPressedKeys().contains(KeyCode.W) || InputManager.getPressedKeys().contains(KeyCode.S)
                || InputManager.getPressedKeys().contains(KeyCode.A) || InputManager.getPressedKeys().contains(KeyCode.D));
    }

    private void spawnBullet(Pane root, int direction) {
        if (canSpawnBullet) {

            File file = new File("resources/game/soundFX/gunshot.wav");
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

            double posX = player.getPosX();
            double posY = player.getPosY();

            if(direction == 90){
                posX += 16;
            } else if(direction == -90) {
                posX -= 16;
            } else if(direction == 0) {
                posY -= 16;
            } else if(direction == 180) {
                posY += 16;
            } else if(direction == 45) {
                posX += 16;
                posY -= 16;
            } else if(direction == -45) {
                posX -= 16;
                posY -= 16;
            } else if(direction == 135) {
                posX += 16;
                posY += 16;
            } else if(direction == -135) {
                posX -= 16;
                posY += 16;
            }

            Bullet newBullet = new Bullet(root, posX, posY, direction);
            bulletManager.addBulletList(newBullet);

            timeSinceLastBullet = 0;
            canSpawnBullet = false;
        }
    }
}
