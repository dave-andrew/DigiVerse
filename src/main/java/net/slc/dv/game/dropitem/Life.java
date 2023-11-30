package net.slc.dv.game.dropitem;

import javafx.scene.image.Image;
import net.slc.dv.game.Enemy;

public class Life extends DropItem {

    private final Image sprite = new Image("file:resources/game/items/life.png");

    public Life(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }
}
