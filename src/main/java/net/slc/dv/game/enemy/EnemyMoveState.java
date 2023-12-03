package net.slc.dv.game.enemy;

import net.slc.dv.game.Enemy;
import net.slc.dv.game.gamestate.GamePauseState;
import net.slc.dv.view.offlinegame.OfflineGameView;

public class EnemyMoveState extends EnemyBaseState {

    private int frame = 0;
    private double lastTimeFrame = 0;

    public EnemyMoveState(Enemy enemy) {
        super(enemy);
    }

    @Override
    public void onEnterState() {
        this.frame = 0;
        this.lastTimeFrame = 0;
    }

    @Override
    public void onUpdate(double deltaTime, OfflineGameView game) {

        this.lastTimeFrame += deltaTime;

        if (lastTimeFrame > 2) {
            this.frame++;
            this.lastTimeFrame = 0;
        }

        if (frame == enemy.getSpriteList().size()) {
            this.frame = 0;
        }

        if (enemy.getHealth() <= 0) {
            enemy.changeState(enemy.getDeadState());
        }

        if (enemy.getPlayer() != null && !(game.getState() instanceof GamePauseState)) {
            double destX = enemy.getPlayer().getPosX();
            double destY = enemy.getPlayer().getPosY();

            double deltaX = destX - enemy.getPosX();
            double deltaY = destY - enemy.getPosY();
            double distanceToPlayer = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distanceToPlayer > 0) {
                deltaX /= distanceToPlayer;
                deltaY /= distanceToPlayer;
            }

            double moveSpeed = enemy.getSpeed();

            enemy.setPosX(enemy.getPosX() + deltaX * moveSpeed * deltaTime);
            enemy.setPosY(enemy.getPosY() + deltaY * moveSpeed * deltaTime);

            if (deltaX < 0) {
                enemy.setScaleX(-2);
            } else {
                enemy.setScaleX(2);
            }

            enemy.setX(enemy.getPosX());
            enemy.setY(enemy.getPosY());

            enemy.setSprite(enemy.getSpriteList().get(frame));
            enemy.setImage(enemy.getSprite());
        }
    }
}
