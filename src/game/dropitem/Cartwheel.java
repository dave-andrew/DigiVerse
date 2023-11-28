package game.dropitem;

import game.Enemy;
import javafx.scene.image.Image;

public class Cartwheel extends DropItem {
    private static final Image sprite = new Image("file:resources/game/items/cartwheel.png");

    public Cartwheel(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);

        setUpImage(sprite);
    }
}
