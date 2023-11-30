package net.slc.dv.game.dropitem;

import javafx.scene.image.Image;
import net.slc.dv.game.Enemy;

public class Cartwheel extends DropItem {
    private final Image sprite = new Image("file:resources/game/items/cartwheel.png");

    public Cartwheel(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }
}
