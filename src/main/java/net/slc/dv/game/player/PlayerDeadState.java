package net.slc.dv.game.player;

import javafx.scene.layout.Pane;
import net.slc.dv.game.Player;

public class PlayerDeadState extends PlayerBaseState {

    private double lastTimeFrame = 0;
    private int frame = 0;

    public PlayerDeadState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {
        this.player.setLives(player.getLives() - 1);
        this.frame = 0;
        this.lastTimeFrame = 0;
    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {
        this.lastTimeFrame += deltaTime;

        if (player.getLives() == -1 && frame == 200) {
            player.changeState(player.getNoLiveState());
            return;
        }

        if (frame == 200) {
            player.changeState(player.getRespawnState());
            return;
        }

        if (lastTimeFrame > 2) {
            this.frame++;
            if (this.frame < 5) {
                this.lastTimeFrame = 0;
            }
        }

        if (frame < 5) {
            player.setSprite(player.getDiedSprites().get(frame));
            player.setImage(player.getSprite());
        } else {
            player.setImage(null);
        }

    }

    @Override
    public void spriteManager(double velocityX, double velocityY, int frame) {

    }
}
