package smartshoppinglist.at.smartshoppinglist.objects;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Item;

public class ItemList {

    private List<Item> items;

    public ItemList(){
        items = new ArrayList<>();
    }

    public void addItem(Item item){
        if(findItemByName(item.getName()) == null){
            items.add(item);
            sort();
            setChanges();
        }
    }
    public void removeItem(Item item){
        items.remove(item);
        setChanges();
    }

    public void addItemList(List<Item> item){
        items.addAll(item);
        sort();
    }

    public List<Item> getItems() {
        return items;
    }

    public Item findItemByName(String name){
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

    private void setChanges(){
        Save.save(this);
    }
    private void sort(){
        Collections.sort(items);
    }
}
