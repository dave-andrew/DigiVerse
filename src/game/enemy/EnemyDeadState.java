package game.enemy;

import game.Enemy;

public class EnemyDeadState extends EnemyBaseState {

    private double lastTimeFrame = 0;
    private int frame = 0;

    public EnemyDeadState(Enemy enemy) {
        super(enemy);
    }

    @Override
    public void onEnterState() {
        this.lastTimeFrame = 0;
        this.frame = 0;
    }

    @Override
    public void onUpdate(double deltaTime) {

        this.lastTimeFrame += deltaTime;

        if (frame == 1000) {
            enemy.changeState(enemy.despawnState);
        }

        if (lastTimeFrame > 2) {
            this.frame++;
            if (this.frame < 6) {
                this.lastTimeFrame = 0;
            }
        }

        if (frame < 5) {
            enemy.setSprite(enemy.getDiedSprites().get(frame));
            enemy.setImage(enemy.getSprite());
        } else {
            enemy.setImage(enemy.getDiedSprites().get(enemy.getDiedSprites().size()-1));
        }

    }
}
