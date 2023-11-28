package game.dropitem;

import game.Enemy;
import javafx.scene.image.Image;

public class Life extends DropItem {
    private static final Image sprite = new Image("file:resources/game/items/life.png");

    public Life(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }
}
