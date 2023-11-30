package net.slc.dv.game.dropitem;

import javafx.scene.image.Image;
import net.slc.dv.game.Enemy;

public class QuickLoad extends DropItem {

    private final Image sprite = new Image("file:resources/game/items/quickload.png");

    public QuickLoad(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }
}
