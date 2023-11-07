package game.dropitem;

import game.Enemy;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Coin1 extends DropItem{
    private final Image sprite = new Image("file:resources/game/items/coin1.png");
    private final int value = 1;
    public Coin1(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }

    public int getValue() {
        return value;
    }
}
