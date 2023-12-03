package net.slc.dv.game.gamestate;

import net.slc.dv.view.offlinegame.OfflineGameView;

public abstract class GameBaseState {

    protected OfflineGameView game;

    public GameBaseState(OfflineGameView game) {
        this.game = game;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(long now);

}
