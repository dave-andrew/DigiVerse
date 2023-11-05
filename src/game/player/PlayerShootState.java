package game.player;

import game.Player;
import helper.InputManager;
import javafx.scene.input.KeyCode;

public class PlayerShootState extends PlayerBaseState{

    private int frame = 0;
    private double lastTimeFrame = 0;

    public PlayerShootState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(double deltaTime) {
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

        if(InputManager.getPressedKeys().contains(KeyCode.RIGHT)){
            player.setSprite(player.getRightSprites().get(frame));
        } else if(InputManager.getPressedKeys().contains(KeyCode.LEFT)){
            player.setSprite(player.getLeftSprites().get(frame));
        } else if(InputManager.getPressedKeys().contains(KeyCode.UP)){
            player.setSprite(player.getUpSprites().get(frame));
        } else if(InputManager.getPressedKeys().contains(KeyCode.DOWN)){
            player.setSprite(player.getDownSprites().get(frame));
        } else if(InputManager.getPressedKeys().isEmpty()){
            player.changeState(player.standState);
            return;
        } else if(!InputManager.getPressedKeys().isEmpty()){
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
}
