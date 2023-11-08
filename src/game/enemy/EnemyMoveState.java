package game.enemy;

import game.Enemy;
import game.gamestate.GamePauseState;
import view.OfflineGame;

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
    public void onUpdate(double deltaTime, OfflineGame game) {

        System.out.println(deltaTime);

        this.lastTimeFrame += deltaTime;

        if (lastTimeFrame > 2) {
            this.frame++;
            this.lastTimeFrame = 0;
        }

        if (frame == 2) {
            this.frame = 0;
        }

        if (enemy.getPlayer() != null && !(game.getState() instanceof GamePauseState)) {
            double destX = enemy.getPlayer().getPosX();
            double destY = enemy.getPlayer().getPosY();

//            System.out.println("Enemy: " + enemy.getPosX() + ", " + enemy.getPosY());

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

            enemy.setX(enemy.getPosX());
            enemy.setY(enemy.getPosY());

            enemy.setSprite(enemy.getSpriteList().get(frame));
            enemy.setImage(enemy.getSprite());
        }
    }
}
