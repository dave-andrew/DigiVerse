package game.dropitem;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Coin5 extends DropItem{

    private final Image sprite = new Image("file:resources/game/items/coin5.png");

    public Coin5(Pane root, double posX, double posY) {
        super(root, posX, posY);

        setUpImage(sprite);
    }
}
