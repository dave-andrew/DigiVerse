package game.dropitem;

import game.Enemy;
import javafx.scene.image.Image;

public class Coin5 extends DropItem {
    private static final Image sprite = new Image("file:resources/game/items/coin5.png");

    public Coin5(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);

        setUpImage(sprite);
    }

    public int getValue() {
        return 5;
    }
}
