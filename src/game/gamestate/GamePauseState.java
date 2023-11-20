package game.gamestate;

import helper.InputManager;
import javafx.scene.input.KeyCode;
import view.OfflineGame;

public class GamePauseState extends GameBaseState {

    public GamePauseState(OfflineGame game) {
        super(game);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(long now) {
        this.game.updateGame(now);
    }
}
