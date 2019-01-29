package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.objects.Item;

public class ItemList {
    List<Item> items;
    public ItemList(){
        items = new ArrayList<>();
    }
    public void addItem(Item item){
        items.add(item);
    }
    public void removeItem(Item item){
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }
    public Item FindItemByName(String name){
        for (Item item:items) {
            if (item.getName().equals(name)){
                return item;
            }
        }
        return null;
    }
    public List<String> getItemNames(){
        List<String> names = new ArrayList<>();
        for (Item item:items) {
            names.add(item.getName());
        }
        return names;
    }
}
