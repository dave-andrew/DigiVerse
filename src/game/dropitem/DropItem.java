package game.dropitem;

import game.Enemy;
import game.dropitem.state.DropItemBaseState;
import game.dropitem.state.DropItemDespawnState;
import game.dropitem.state.DropItemSpawnState;
import helper.Collider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DropItem extends ImageView {

    protected Collider collider;
    protected Enemy enemy;

    protected Image sprite;

    protected double posX;
    protected double posY;

//    Item State
    private DropItemBaseState currentState;
    public DropItemSpawnState spawnState;
    public DropItemDespawnState despawnState;


    public DropItem(Enemy enemy, double posX, double posY) {
        this.enemy = enemy;

        this.spawnState = new DropItemSpawnState(enemy, this);
        this.despawnState = new DropItemDespawnState(enemy, this);

        this.currentState = this.spawnState;

        this.posX = posX;
        this.posY = posY;
    }

    public void changeState(DropItemBaseState newState) {
        this.currentState = newState;
        this.currentState.onEnterState();
    }

    protected void setUpImage(Image image) {
        this.sprite = image;
        this.setImage(sprite);

        this.collider = new Collider(posX, posY, sprite.getWidth());

        this.setScaleX(2);
        this.setScaleY(2);

        this.setX(posX);
        this.setY(posY);

        this.enemy.getRoot().getChildren().add(this);
    }

    public Collider getCollider() {
        return collider;
    }

    public DropItemBaseState getState() {
        return this.currentState;
    }

}
