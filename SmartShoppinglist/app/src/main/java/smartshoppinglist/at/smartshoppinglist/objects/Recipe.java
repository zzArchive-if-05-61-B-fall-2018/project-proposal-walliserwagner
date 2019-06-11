package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipe implements Serializable {
    private String name;
    private String description;
    private List<ItemContainer> items;

    public Recipe(String name, String description, List<ItemContainer> items) {
        this.name = name;
        this.description = description;
        this.items = items;
    }

    public Recipe(Recipe recipe) {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.items = recipe.getItems();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemContainer> getItems() {
        return items;
    }

    public void setItems(List<ItemContainer> items) {
        this.items = items;
    }
    public String[] getItemNames(){
        List<String> result = new ArrayList<>();
        for (ItemContainer itemContainer:items) {
            result.add(itemContainer.getItem().getName());
        }
        return result.toArray(new String[0]);
    }

    public void addItem(ItemContainer itemContainer){
        itemContainer.setTicked(true);
        for (ItemContainer ic:items) {
            if(ic.getItem().getName().equals(itemContainer.getItem().getName()) && ic.getUnit().equals(itemContainer.getUnit())){
                ic.setCount(ic.getCount()+itemContainer.getCount());
                return;
            }
        }
        items.add(itemContainer);
        sort();
    }
    public void removeItem(ItemContainer itemContainer){
        items.remove(itemContainer);
    }
    private void sort(){
        Collections.sort(items);
    }
    public ItemContainer getItemByNameAndUnit(String name, String unit){
        for (ItemContainer ic:items) {
            if(ic.getItem().getName().equals(name) && ic.getUnit().equals(unit)){
                return ic;
            }
        }
        return null;
    }
}
