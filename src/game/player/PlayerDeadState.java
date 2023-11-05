package game.player;

import game.Player;
import javafx.scene.layout.Pane;

public class PlayerDeadState extends PlayerBaseState{

    private double lastTimeFrame = 0;
    private int frame = 0;

    public PlayerDeadState(Player player) {
        super(player);
    }

    @Override
    public void onEnterState() {
        player.setLives(player.getLives() - 1);
        this.frame = 0;
        this.lastTimeFrame = 0;
    }

    @Override
    public void onUpdate(double deltaTime, Pane root) {
        this.lastTimeFrame += deltaTime;

        if(frame == 100) {
            player.changeState(player.respawnState);
        }

        if (lastTimeFrame > 2) {
            this.frame++;
            if(this.frame < 5){
                this.lastTimeFrame = 0;
            }
        }

        if(frame < 5) {
            player.setSprite(player.getDiedSprites().get(frame));
            player.setImage(player.getSprite());
        } else {
            player.setImage(null);
        }

    }
}
