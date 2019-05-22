package smartshoppinglist.at.smartshoppinglist.objects;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.server.Server;

public class ItemList {

    private List<Item> items;

    public ItemList(){
        items = new ArrayList<>();
    }

    public void addItem(Item item){
        if(findItemByName(item.getName()) == null){
            items.add(item);
            sort();
            Server.getInstance().postRequest("/itemlist", String.format("{\"userid\":\"%d\",\"name\":\"%s\",\"defunit\":\"%s\",\"category\":\"%s\"}", MainActivity.getInstance().getCurrentUser().getId(), item.getName(), item.getDefaultUnit(), item.getCategory()));
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

    public List<Item> getItemsByCategory(String name){
        List<Item> result = new ArrayList<>();
        for (Item item :items) {
            if(item.getCategory().equals(name)) result.add(item);
        }
        return result;
    }
}
