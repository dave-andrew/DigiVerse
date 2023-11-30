package net.slc.dv.game.gamestate;

import net.slc.dv.view.OfflineGame;

public abstract class GameBaseState {

    protected OfflineGame game;

    public GameBaseState(OfflineGame game) {
        this.game = game;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(long now);

}
