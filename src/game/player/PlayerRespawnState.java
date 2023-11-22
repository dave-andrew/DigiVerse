package game.player;

import game.Player;
import helper.ScreenManager;
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

        player.setPosX(ScreenManager.SCREEN_WIDTH / 2);
        player.setPosY(ScreenManager.SCREEN_HEIGHT / 2);

        player.setX(player.getPosX());
        player.setY(player.getPosY());

        player.setImage(player.getSprite());
        player.changeState(player.standState);
    }

    @Override
    public void spriteManager(double velocityX, double velocityY, int frame) {

    }
}
