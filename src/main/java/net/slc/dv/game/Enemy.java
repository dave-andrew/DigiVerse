package net.slc.dv.game;

import lombok.Getter;
import net.slc.dv.game.enemy.EnemyBaseState;
import net.slc.dv.game.enemy.EnemyDeadState;
import net.slc.dv.game.enemy.EnemyDespawnState;
import net.slc.dv.game.enemy.EnemyMoveState;
import net.slc.dv.helper.Collider;
import net.slc.dv.helper.ImageManager;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import net.slc.dv.view.offlinegame.OfflineGameView;

import java.util.ArrayList;

public class Enemy extends ImageView {

    private final OfflineGameView game;
    private final Player player;

    private int health;
    private double posX;
    private double posY;

    private final Collider collider;

    private double speed;
    private Image sprite;

    private EnemyBaseState currentState;
    private final EnemyDeadState deadState;
    private final EnemyDespawnState despawnState;

    private ArrayList<Image> spriteList;

    private final ArrayList<Image> diedSprites;

    private final Pane root;
    private final String type;
    public Enemy(OfflineGameView game, Pane root, double posX, double posY, Player player, String type, int health) {
        this.game = game;
        this.player = player;
        this.type = type;
        this.speed = (type.equals("spider")) ? 10 : 3;

        this.root = root;
        this.health = health;

        initSprite(type);

        this.diedSprites = ImageManager.importEnemyDiedSprites();

        this.posX = posX;
        this.posY = posY;

        this.setX(posX);
        this.setY(posY);

        this.sprite = new Image("file:resources/game/enemy/soldier-1.png");

        this.collider = new Collider(posX, posY);

        EnemyMoveState moveState = new EnemyMoveState(this);
        this.deadState = new EnemyDeadState(this);
        this.despawnState = new EnemyDespawnState(this);

        this.currentState = moveState;

        this.setImage(sprite);
        root.getChildren().add(this);

        this.setScaleX(2);
        this.setScaleY(2);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(-5.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));

        this.setEffect(dropShadow);

    }

    public ArrayList<Image> getDiedSprites() {
        return this.diedSprites;
    }

    private void initSprite(String type) {
        switch (type) {
            case "soldier":
                this.spriteList = ImageManager.importEnemySprites("soldier");
                break;
            case "mummy":
                this.spriteList = ImageManager.importEnemySprites("mummy");
                break;
            case "bug":
                this.spriteList = ImageManager.importEnemySprites("bug");
                break;
            case "spider":
                this.spriteList = ImageManager.spider("spider");
                break;
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

    public Pane getRoot() {
        return root;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getType() {
        return type;
    }

    public EnemyDeadState getDeadState() {
        return deadState;
    }

    public EnemyDespawnState getDespawnState() {
        return despawnState;
    }

    public OfflineGameView getGame() {
        return game;
    }
}
