package smartshoppinglist.at.smartshoppinglist;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Shoppinglist {
    private List<Category<ItemContainer>> items;
    private String name;

    public Shoppinglist(String name){
        this.name = name;
        items = new ArrayList<Category<ItemContainer>>();
    }
    public Category<ItemContainer>[] getItems(){
        return items.toArray((Category<ItemContainer>[])Array.newInstance(items.get(0).getClass(),items.size()));
    }
    public void addCategory(Category<ItemContainer> c){
        items.add(c);
    }

    public String getName() {
        return name;
    }
}
