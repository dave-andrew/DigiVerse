package game.dropitem;

import helper.Collider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DropItem extends ImageView {

    protected Collider collider;
    protected Pane root;

    protected Image sprite;

    protected double posX;
    protected double posY;

    public DropItem(Pane root, double posX, double posY) {
        this.root = root;

        this.posX = posX;
        this.posY = posY;
    }

    protected void setUpImage(Image image) {
        this.sprite = image;
        this.setImage(sprite);

        this.collider = new Collider(posX, posY, sprite.getWidth());

        this.setScaleX(2);
        this.setScaleY(2);

        this.setX(posX);
        this.setY(posY);

        root.getChildren().add(this);
    }

    public Collider getCollider() {
        return collider;
    }

}
