package net.slc.dv.game.dropitem;

import net.slc.dv.game.Enemy;
import javafx.scene.image.Image;

public class Nuke extends DropItem {
    private static final Image sprite = new Image("file:resources/game/items/nuke.png");

    public Nuke(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }
}
