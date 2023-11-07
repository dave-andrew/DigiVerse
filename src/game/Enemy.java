package game;

import game.enemy.EnemyBaseState;
import game.enemy.EnemyDeadState;
import game.enemy.EnemyMoveState;
import helper.Collider;
import helper.ImageManager;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Enemy extends ImageView {

    private Player player;

    private double posX;
    private double posY;

    private Collider collider;

    private double speed;
    private Image sprite;

    private EnemyBaseState currentState;
    public EnemyMoveState moveState;
    public EnemyDeadState deadState;

    private ArrayList<Image> spriteList;

    private long lastTimeFrame = 0;

    public Enemy(Pane root, double posX, double posY, Player player, String type) {

        this.player = player;

        initSprite(type);

        this.collider = new Collider(posX, posY);

        this.posX = posX;
        this.posY = posY;

        this.setX(posX);
        this.setY(posY);

        this.speed = 3;
        this.sprite = new Image("file:resources/game/enemy/soldier-1.png");

        this.moveState = new EnemyMoveState(this);
        this.deadState = new EnemyDeadState(this);

        this.currentState = this.moveState;

//        AnimationTimer run = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if(lastTimeFrame == 0) {
//                    lastTimeFrame = now;
//                } else if(currentState instanceof EnemyDeadState) {
//                    root.getChildren().remove(Enemy.this);
//                    this.stop();
//                }
//
//                double deltaTime = (double) (now - lastTimeFrame) / 50_000_000;
//
//                currentState.onUpdate(deltaTime);
//
//                collider.setCollider(posX);
//
//                lastTimeFrame = now;
//            }
//        };
//
//        run.start();

        this.setImage(sprite);
        root.getChildren().add(this);

        this.setScaleX(2);
        this.setScaleY(2);
    }

    private void initSprite(String type) {
        if(type.equals("soldier")) {
            this.spriteList = ImageManager.importEnemySprites("soldier");
        }
    }

    public void changeState(EnemyBaseState state) {
        this.currentState = state;
        this.currentState.onEnterState();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public Player getPlayer() {
        return player;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public ArrayList<Image> getSpriteList() {
        return spriteList;
    }

    public Collider getCollider() {
        return this.collider;
    }

    public EnemyBaseState getState() {
        return this.currentState;
    }
}
