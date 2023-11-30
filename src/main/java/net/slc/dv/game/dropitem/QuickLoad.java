package net.slc.dv.game.dropitem;

import net.slc.dv.game.Enemy;
import javafx.scene.image.Image;

public class QuickLoad extends DropItem {
    private static final Image sprite = new Image("file:resources/game/items/quickload.png");

    public QuickLoad(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);

        setUpImage(sprite);
    }
}
