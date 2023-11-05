package game.player;

import game.Player;
import helper.InputManager;
import javafx.scene.input.KeyCode;

public class PlayerWalkState extends PlayerBaseState {

    public PlayerWalkState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {
        System.out.println("Walk State");
    }

    @Override
    public void onUpdate() {

        if(InputManager.getPressedKeys().isEmpty()) {
            player.changeState(player.standState);
            return;
        } else if(player.getVelocityX() == 0 && player.getVelocityY() != 0) {
            if(player.getVelocityY() > 0) {
                player.setSprite(player.getDownSprites().get(0));
            } else {
                player.setSprite(player.getUpSprites().get(0));
            }
        } else if(player.getVelocityX() != 0 && player.getVelocityY() == 0) {
            if(player.getVelocityX() > 0) {
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(0));
            }
        } else {
            if(player.getVelocityX() > 0) {
                player.setSprite(player.getRightSprites().get(0));
            } else {
                player.setSprite(player.getLeftSprites().get(0));
            }
        }

        if (InputManager.getPressedKeys().contains(KeyCode.D)) {
            player.setVelocityX(player.getSpeed());
        } else if (InputManager.getPressedKeys().contains(KeyCode.A)) {
            player.setVelocityX(-player.getSpeed());
        } else {
            player.setVelocityX(0);
        }

        if (InputManager.getPressedKeys().contains(KeyCode.W)) {
            player.setVelocityY(-player.getSpeed());
        } else if (InputManager.getPressedKeys().contains(KeyCode.S)) {
            player.setVelocityY(player.getSpeed());
        } else {
            player.setVelocityY(0);
        }

        System.out.println(player.getVelocityX() + " " + player.getVelocityY());

        player.setPosX(player.getPosX() + player.getVelocityX());
        player.setPosY(player.getPosY() + player.getVelocityY());

        player.setX(player.getPosX());
        player.setY(player.getPosY());

        player.setImage(player.getSprite());
    }
}
