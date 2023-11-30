package net.slc.dv.game.gamestate;

import net.slc.dv.game.player.PlayerNoLiveState;
import net.slc.dv.view.OfflineGame;

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
            this.game.changeState(this.game.getOverState());
            return;
        }

        this.game.updateGame(now);
    }
}
