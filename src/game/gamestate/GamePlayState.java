package game.gamestate;

import game.player.PlayerNoLiveState;
import helper.InputManager;
import javafx.scene.input.KeyCode;
import view.OfflineGame;

public class GamePlayState extends GameBaseState {

    public GamePlayState(OfflineGame game) {
        super(game);
    }

    @Override
    public void onEnterState() {
        game.getMediaPlayer().play();
    }

    @Override
    public void onUpdate(long now) {
        if (game.getPlayer().getState() instanceof PlayerNoLiveState) {
            game.setBatchTimer(game.getInitialTimer());
            this.game.changeState(this.game.overState);
            return;
        }

        this.game.updateGame(now);
    }
}
