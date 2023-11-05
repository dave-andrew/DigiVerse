package game.player;

import game.Bullet;
import game.Player;
import helper.InputManager;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class PlayerShootState extends PlayerBaseState {
    private int frame = 0;
    private double lastTimeFrame = 0;
    private double bulletCooldown = 3;
    private double timeSinceLastBullet = 0;
    private boolean canSpawnBullet = true;

    public PlayerShootState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {

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

        double speed = player.getSpeed();
        double velocityX = 0;
        double velocityY = 0;

        if (InputManager.getPressedKeys().contains(KeyCode.D)) {
            velocityX += speed;
        }
        if (InputManager.getPressedKeys().contains(KeyCode.A)) {
            velocityX -= speed;
        }

        if (InputManager.getPressedKeys().contains(KeyCode.W)) {
            velocityY -= speed;
        }
        if (InputManager.getPressedKeys().contains(KeyCode.S)) {
            velocityY += speed;
        }

        if (velocityX != 0 && velocityY != 0) {
            double length = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            velocityX = (velocityX / length) * speed;
            velocityY = (velocityY / length) * speed;
        }

        if(InputManager.getPressedKeys().contains(KeyCode.UP) && InputManager.getPressedKeys().contains(KeyCode.RIGHT)){
            if (validateMove()) {
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getRightSprites().get(frame));
            }

            spawnBullet(root, "up-right");

        } else if(InputManager.getPressedKeys().contains(KeyCode.UP) && InputManager.getPressedKeys().contains(KeyCode.LEFT)){
            if (validateMove()) {
                player.setSprite(player.getLeftSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(frame));
            }

            spawnBullet(root, "up-left");

        } else if(InputManager.getPressedKeys().contains(KeyCode.DOWN) && InputManager.getPressedKeys().contains(KeyCode.RIGHT)){
            if (validateMove()) {
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getRightSprites().get(frame));
            }

            spawnBullet(root, "down-right");

        } else if(InputManager.getPressedKeys().contains(KeyCode.DOWN) && InputManager.getPressedKeys().contains(KeyCode.LEFT)) {
            if (validateMove()) {
                player.setSprite(player.getLeftSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(frame));
            }

            spawnBullet(root, "down-left");
        } else if (InputManager.getPressedKeys().contains(KeyCode.RIGHT)){
            if (validateMove()){
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getRightSprites().get(frame));
            }

            spawnBullet(root, "right");

        } else if (InputManager.getPressedKeys().contains(KeyCode.LEFT)){
            if (validateMove()){
                player.setSprite(player.getLeftSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(frame));
            }

            spawnBullet(root, "left");

        } else if (InputManager.getPressedKeys().contains(KeyCode.UP)){
            if (validateMove()) {
                player.setSprite(player.getUpSprites().get(0));
            } else {
                player.setSprite(player.getUpSprites().get(frame));
            }

            spawnBullet(root, "up");

        } else if (InputManager.getPressedKeys().contains(KeyCode.DOWN)) {
            if (validateMove()) {
                player.setSprite(player.getDownSprites().get(0));
            } else {
                player.setSprite(player.getDownSprites().get(frame));
            }

            spawnBullet(root, "down");

        } else if (InputManager.getPressedKeys().isEmpty()){
            player.changeState(player.standState);
            return;
        } else if (!InputManager.getPressedKeys().isEmpty()){
            player.changeState(player.walkState);
            return;
        }

        player.setVelocityX(velocityX);
        player.setVelocityY(velocityY);

        player.setPosX(player.getPosX() + player.getVelocityX() * deltaTime);
        player.setPosY(player.getPosY() + player.getVelocityY() * deltaTime);

        player.setX(player.getPosX());
        player.setY(player.getPosY());

        player.setImage(player.getSprite());
    }

    private boolean validateMove() {
        return !(InputManager.getPressedKeys().contains(KeyCode.W) || InputManager.getPressedKeys().contains(KeyCode.S)
                || InputManager.getPressedKeys().contains(KeyCode.A) || InputManager.getPressedKeys().contains(KeyCode.D));
    }

    private void spawnBullet(Pane root, String direction) {
        if (canSpawnBullet) {

            double posX = player.getPosX();
            double posY = player.getPosY();

            if(direction.equals("right")){
                posX += 16;
            } else if(direction.equals("left")) {
                posX -= 16;
            } else if(direction.equals("up")) {
                posY -= 16;
            } else if(direction.equals("down")) {
                posY += 16;
            } else if(direction.equals("up-right")) {
                posX += 16;
                posY -= 16;
            } else if(direction.equals("up-left")) {
                posX -= 16;
                posY -= 16;
            } else if(direction.equals("down-right")) {
                posX += 16;
                posY += 16;
            } else if(direction.equals("down-left")) {
                posX -= 16;
                posY += 16;
            }

            Bullet newBullet = new Bullet(root, posX, posY, direction);

//            Keknya bakal tetep harus di store di arraylist supaya bisa cek collision ntar

            timeSinceLastBullet = 0;
            canSpawnBullet = false;
        }
    }
}
