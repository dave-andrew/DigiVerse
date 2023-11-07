package game.dropitem;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Coin1 extends DropItem{
    private final Image sprite = new Image("file:resources/game/items/coin1.png");

    public Coin1(Pane root, double posX, double posY) {
        super(root, posX, posY);
        setUpImage(sprite);
    }
}
