package game.player;

import game.Player;
import helper.InputManager;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class PlayerStandState extends PlayerBaseState {

    public PlayerStandState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {
        System.out.println("Stand State");
    }

    @Override
    public void onUpdate(double deltaTime) {
        ArrayList<KeyCode> pressedKeys = InputManager.getPressedKeys();
        if(!pressedKeys.isEmpty()){
            player.changeState(player.walkState);
        }
    }
}
