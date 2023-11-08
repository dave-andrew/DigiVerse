package game.gamestate;

import helper.InputManager;
import javafx.scene.input.KeyCode;
import view.OfflineGame;

public class GamePauseState extends GameBaseState{
    public GamePauseState(OfflineGame game) {
        super(game);
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(long now) {
        if(InputManager.getPressedKeys().contains(KeyCode.ESCAPE)) {
            this.game.changeState(this.game.playState);
        }
        this.game.updateGame(now);
    }
}
