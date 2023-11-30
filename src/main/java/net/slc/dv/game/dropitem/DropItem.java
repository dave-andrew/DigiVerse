package net.slc.dv.game.dropitem;

import net.slc.dv.game.Enemy;
import net.slc.dv.game.dropitem.state.DropItemBaseState;
import net.slc.dv.game.dropitem.state.DropItemDespawnState;
import net.slc.dv.game.dropitem.state.DropItemSpawnState;
import net.slc.dv.helper.Collider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class DropItem extends ImageView {

    private final DropItemDespawnState despawnState;
    protected Collider collider;
    protected Enemy enemy;
    protected Image sprite;
    protected double posX;
    protected double posY;

    //    Item State
    private DropItemBaseState currentState;


    public DropItem(Enemy enemy, double posX, double posY) {
        this.enemy = enemy;

        DropItemSpawnState spawnState = new DropItemSpawnState(enemy.getGame(), enemy, this);
        this.despawnState = new DropItemDespawnState(enemy.getGame(), enemy, this);

        this.currentState = spawnState;

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

    public DropItemDespawnState getDespawnState() {
        return despawnState;
    }
}
