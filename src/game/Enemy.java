package game;

import helper.ImageManager;
import javafx.scene.image.Image;

public class Enemy {

    private double speed;
    private Image sprite;

    public Enemy() {
        this.speed = 3;
        this.sprite = new Image("file:resources/icons/user.png");
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }
}
