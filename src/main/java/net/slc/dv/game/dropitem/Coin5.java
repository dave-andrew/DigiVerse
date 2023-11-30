package net.slc.dv.game.dropitem;

import javafx.scene.image.Image;
import net.slc.dv.game.Enemy;

public class Coin5 extends DropItem {

    private final Image sprite = new Image("file:resources/game/items/coin5.png");
    private final int value = 5;

    public Coin5(Enemy enemy, double posX, double posY) {
        super(enemy, posX, posY);
        setUpImage(sprite);
    }

    public int getValue() {
        return value;
    }
}
