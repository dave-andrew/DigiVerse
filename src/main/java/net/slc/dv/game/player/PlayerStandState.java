package net.slc.dv.game.player;

import net.slc.dv.game.Player;
import net.slc.dv.helper.InputManager;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.Set;

public class PlayerStandState extends PlayerBaseState {

    public PlayerStandState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {

        Set<KeyCode> pressedKeys = InputManager.getPressedKeys();
        if (!pressedKeys.isEmpty()) {
            player.changeState(player.getWalkState());
        }
    }

    @Override
    public void spriteManager(double velocityX, double velocityY, int frame) {

    }
}
