package game.player;

import game.Player;
import javafx.scene.layout.Pane;

public class PlayerNoLiveState extends PlayerBaseState{
    public PlayerNoLiveState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {

    }
}
