package game.dropitem;

import game.Enemy;
import javafx.scene.image.Image;

public class ThreeShot extends DropItem{
    private final Image sprite = new Image("file:resources/game/items/threeshot.png");
    public ThreeShot(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }
}
