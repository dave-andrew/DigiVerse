package game.player;

import game.Player;
import javafx.scene.layout.Pane;

public class PlayerRespawnState extends PlayerBaseState {
    public PlayerRespawnState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {
        player.setSprite(player.getDownSprites().get(0));
        player.setImage(player.getSprite());
        player.changeState(player.standState);
    }
}
