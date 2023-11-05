package game.player;

import game.Bullet;
import game.Player;
import game.bullet.BulletStopState;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class PlayerBaseState {

    protected Player player;

    public PlayerBaseState(Player player) {
        this.player = player;
    }

    public abstract void onEnterState();

    public abstract void onUpdate(double deltaTime, Pane root);
//
//    public void displayBullet(double deltaTime, Pane root) {
//        if (!bullets.isEmpty()) {
//            Iterator<Bullet> iterator = bullets.iterator();
//            while (iterator.hasNext()) {
//                Bullet bullet = iterator.next();
//                if (bullet.getState() instanceof BulletStopState) {
//                    iterator.remove(); // Remove the bullet using the iterator
//                } else {
//                    bullet.getState().onUpdate(deltaTime);
//                }
//            }
//        }
//    }
}
