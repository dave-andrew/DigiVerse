package helper;

import game.dropitem.DropItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemManager {
    private static ItemManager instance;
    private List<DropItem> itemList;

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    private ItemManager() {
        this.itemList = Collections.synchronizedList(new ArrayList<>());
    }

    public List<DropItem> getItemList() {
        return itemList;
    }

    public void addDropItem(DropItem newItem) {
        this.itemList.add(newItem);
    }

    public void removeItem(DropItem item) {
        this.itemList.remove(itemList.indexOf(item));
    }

}
