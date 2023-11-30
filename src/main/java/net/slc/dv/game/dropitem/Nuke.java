package net.slc.dv.game.dropitem;

import javafx.scene.image.Image;
import net.slc.dv.game.Enemy;

public class Nuke extends DropItem {
    private final Image sprite = new Image("file:resources/game/items/nuke.png");

    public Nuke(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }
}
