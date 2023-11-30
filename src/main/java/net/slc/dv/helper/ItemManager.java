package net.slc.dv.helper;

import lombok.Getter;
import net.slc.dv.game.dropitem.DropItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ItemManager {
    private static ItemManager instance;
    private List<DropItem> itemList;

    private ItemManager() {
        this.itemList = Collections.synchronizedList(new ArrayList<>());
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    public void addDropItem(DropItem newItem) {
        this.itemList.add(newItem);
    }

    public void removeItem(DropItem item) {
        this.itemList.remove(itemList.indexOf(item));
    }

}
