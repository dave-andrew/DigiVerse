package net.slc.dv.game.player;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import net.slc.dv.game.Player;
import net.slc.dv.helper.InputManager;

import java.util.ArrayList;

public class PlayerStandState extends PlayerBaseState {

    public PlayerStandState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {
//        System.out.println("Stand State");
    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {

        ArrayList<KeyCode> pressedKeys = InputManager.getPressedKeys();
        if (!pressedKeys.isEmpty()) {
            player.changeState(player.walkState);
        }
    }

    @Override
    public void spriteManager(double velocityX, double velocityY, int frame) {

    }
}
