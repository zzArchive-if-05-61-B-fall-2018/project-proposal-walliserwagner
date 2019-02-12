package smartshoppinglist.at.smartshoppinglist.objects;

import android.content.Context;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;

public class Shoppinglist {
    private List<Category<ItemContainer>> items;
    private String name;
    private String categoryBought;
    private Context context;

    public Shoppinglist(String name){
        this.name = name;
        items = new ArrayList<Category<ItemContainer>>();
        addCategory(new Category<ItemContainer>(ItemContainer.class,"Allgemein",-1,true));
        addCategory(new Category<ItemContainer>(ItemContainer.class,"Gekauft",-2,true));
        categoryBought = "Gekauft";
    }
    public Category<ItemContainer>[] getItems(){
        List<Category<ItemContainer>> result = new ArrayList<>();
        for (Category<ItemContainer> category:items) {
            if (category.getElements().length > 0){
                result.add(category);
            }
        }
        return result.toArray((Category<ItemContainer>[])Array.newInstance(items.get(0).getClass(),result.size()));
    }
    public void addCategory(Category<ItemContainer> c){
        items.add(c);
        sort();
    }

    public String getName() {
        return name;
    }

    public void addItem(ItemContainer itemContainer){
        Category<ItemContainer> category = getCategoryByName(itemContainer.getItem().getCategory());
        if(category == null){
            category = new Category<ItemContainer>(ItemContainer.class,itemContainer.getItem().getCategory(),true);
            addCategory(category);
        }
        category.addElement(itemContainer);
        category.sort();
        try {
            Save.save(context, new String[]{"title", "amount", "unit"}, itemContainer.getItem().getName(), itemContainer.getCount(), itemContainer.getUnit());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeItem(ItemContainer itemContainer){
        Category<ItemContainer> category;
        if(itemContainer.isTicked() == true){
            category = getCategoryByName(categoryBought);
        }
        else {
            category = getCategoryByName(itemContainer.getItem().getCategory());
        }
        if(category != null){
            category.removeElement(itemContainer);
        }
    }
    private Category<ItemContainer> getCategoryByName(String name){
        for (Category<ItemContainer> category:items) {
            if (category.getName().equals(name)){
                return  category;
            }
        }
        return null;
    }
    public void tickItem(ItemContainer itemContainer){
        itemContainer.setTicked(true);
        Category<ItemContainer> category = getCategoryByName(itemContainer.getItem().getCategory());
        if (category != null && category.containsElement(itemContainer)){
            category.removeElement(itemContainer);
            getCategoryByName(categoryBought).addElement(itemContainer);
            getCategoryByName(categoryBought).sort();
        }
    }
    public void unTickItem(ItemContainer itemContainer){
        itemContainer.setTicked(false);
        Category<ItemContainer> category = getCategoryByName(categoryBought);
        if (category != null && category.containsElement(itemContainer)){
            category.removeElement(itemContainer);
            addItem(itemContainer);
        }
    }
    public void removeTickedItems(){
        Category<ItemContainer> category = getCategoryByName(categoryBought);
        if (category != null){
            category.clear();
        }
    }
    public ItemContainer getItemByPos(int x, int y){
        return (ItemContainer) getItems()[x].getElements()[y];
    }
    public void sort(){
        Collections.sort(items);
    }
}
