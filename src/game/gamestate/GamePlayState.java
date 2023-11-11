package game.gamestate;

import game.player.PlayerNoLiveState;
import helper.InputManager;
import javafx.scene.input.KeyCode;
import view.OfflineGame;

public class GamePlayState extends GameBaseState{
    public GamePlayState(OfflineGame game) {
        super(game);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(long now) {

        if(game.getPlayer().getState() instanceof PlayerNoLiveState) {
            this.game.changeState(this.game.overState);
            return;
        }

        if(InputManager.getPressedKeys().contains(KeyCode.ESCAPE)) {
            this.game.changeState(this.game.pauseState);
            return;
        }

        this.game.updateGame(now);
    }
}