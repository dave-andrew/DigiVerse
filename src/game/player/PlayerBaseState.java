package game.player;

import game.Player;

public abstract class PlayerBaseState {

    protected Player player;

    public PlayerBaseState(Player player) {
        this.player = player;
    }

    public abstract void onEnterState();

    public abstract void onUpdate();
}
