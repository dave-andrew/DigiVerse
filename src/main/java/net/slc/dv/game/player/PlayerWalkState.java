package net.slc.dv.game.player;

import net.slc.dv.game.Player;
import net.slc.dv.helper.InputManager;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class PlayerWalkState extends PlayerBaseState {

    private int frame = 0;
    private double lastTimeFrame = 0;

    public PlayerWalkState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {
//        System.out.println("Walk State");
    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {

        this.lastTimeFrame += deltaTime;

        if (lastTimeFrame > 3) {
            this.frame++;
            this.lastTimeFrame = 0;
        }

        if (frame == 4) {
            this.frame = 0;
        }

        if (InputManager.getPressedKeys().contains(KeyCode.UP) || InputManager.getPressedKeys().contains(KeyCode.DOWN) ||
                InputManager.getPressedKeys().contains(KeyCode.RIGHT) || InputManager.getPressedKeys().contains(KeyCode.LEFT)) {
            player.changeState(player.getShootState());
            return;
        }

        walk(deltaTime, frame);
    }

    @Override
    public void spriteManager(double velocityX, double velocityY, int frame) {
        if (InputManager.getPressedKeys().isEmpty()) {
            player.changeState(player.getStandState());
            return;
        }

        if (velocityX == 0 && velocityY != 0) {
            if (velocityY > 0) {
                player.setSprite(player.getDownSprites().get(frame));
                return;
            }

            player.setSprite(player.getUpSprites().get(frame));
            return;
        }

        if (velocityX > 0) {
            player.setSprite(player.getRightSprites().get(frame));
            return;
        }

        player.setSprite(player.getLeftSprites().get(frame));
    }
}
