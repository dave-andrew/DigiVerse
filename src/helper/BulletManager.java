package helper;

import game.Bullet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BulletManager {

    private static BulletManager instance;
    private final List<Bullet> bulletList;

    public static BulletManager getInstance() {
        if (instance == null) {
            instance = new BulletManager();
        }
        return instance;
    }

    private BulletManager() {
        this.bulletList = Collections.synchronizedList(new ArrayList<>());
    }

    public List<Bullet> getBulletList() {
        return bulletList;
    }

    public void addBulletList(Bullet newBullet) {
        this.bulletList.add(newBullet);
    }
    public void removeAllBullet(ArrayList<Bullet> bullet) {
        for (Bullet b : bullet) {
            this.bulletList.remove(bulletList.indexOf(b));
        }
    }
}
