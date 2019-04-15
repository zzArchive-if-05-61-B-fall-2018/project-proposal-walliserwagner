package smartshoppinglist.at.smartshoppinglist.objects;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;

public class Shoppinglist implements Serializable {
    private List<Category<ItemContainer>> items;

    public void setName(String name) {
        this.name = name;
    }
    private Group group;

    private String name;
    private static String categoryBought = "Gekauft";

    private static String categoryGeneral = "Alegmein";

<<<<<<< HEAD
    public Shoppinglist(String name, Group group){
        this.group = group;
=======
    private boolean isDefault = false;

    public Shoppinglist(String name){
>>>>>>> 7fd570ae8f0a124fb2abb794e79fdc8b4ad45731
        this.name = name;
        items = new ArrayList<Category<ItemContainer>>();
        addCategory(new Category<ItemContainer>(ItemContainer.class,categoryGeneral,-1,true));
        addCategory(new Category<ItemContainer>(ItemContainer.class,categoryBought,-2,true));
    }

    public Shoppinglist(String name, boolean isDefault){
        this(name);
        this.isDefault = isDefault;
    }

    public void addItemList(List<ItemContainer> itemContainers){
        for (ItemContainer itemContainer:
             itemContainers) {
            Category<ItemContainer> category = null;
            if(itemContainer.isTicked()){
                category = getCategoryByName(categoryBought);

            }else {
                category = getCategoryByName(itemContainer.getItem().getCategory());
            }
            if(category == null){
                category = new Category<ItemContainer>(ItemContainer.class,itemContainer.getItem().getCategory(),true);
                addCategory(category);
            }
            category.addElement(itemContainer);
            category.sort();
        }
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
        addExistingItems(itemContainer);
        category.addElement(itemContainer);
        category.sort();
        setChanges();
    }

    private void setChanges(){
        //Save.save(this);
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
        setChanges();
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
        setChanges();
    }
    public void unTickItem(ItemContainer itemContainer){
        itemContainer.setTicked(false);
        Category<ItemContainer> category = getCategoryByName(categoryBought);
        if (category != null && category.containsElement(itemContainer)){
            category.removeElement(itemContainer);
            addItem(itemContainer);
        }
        setChanges();
    }
    public void removeTickedItems(){
        Category<ItemContainer> category = getCategoryByName(categoryBought);
        if (category != null){
            category.clear();
        }
        setChanges();
    }
    public ItemContainer getItemByPos(int x, int y){
        return (ItemContainer) getItems()[x].getElements()[y];
    }
    public void sort(){
        Collections.sort(items);
    }

    public void updateCategoriesedItems(){
        for (Category<ItemContainer> category: items) {
            ItemContainer[] elements = category.getElements();
            for (int i = 0; i < elements.length; i++) {
                if(!elements[i].isTicked() && !elements[i].getItem().getCategory().equals(category.getName())){
                    category.removeElement(elements[i]);
                    i--;
                    addItem(elements[i]);
                }
            }
        }
    }
    private void addExistingItems(ItemContainer itemContainer){
        Category<ItemContainer> category = getCategoryByName(itemContainer.getItem().getCategory());
        for (int i = 0; category.getElements().length > i; i++) {
            ItemContainer ic = (ItemContainer) category.getElements()[i];
            if(ic.getItem().getName().equals(itemContainer.getItem().getName()) && ic.getUnit().equals(itemContainer.getUnit())){
                itemContainer.setCount(ic.getCount()+itemContainer.getCount());
                category.removeElement(ic);
                i--;
            }
        }
    }
    public static String getCategoryBought() {
        return categoryBought;
    }

    public static void setCategoryBought(String categoryBought) {
        Shoppinglist.categoryBought = categoryBought;
    }

    public static String getCategoryGeneral() {
        return categoryGeneral;
    }

    public static void setCategoryGeneral(String categoryGeneral) {
        Shoppinglist.categoryGeneral = categoryGeneral;
    }
<<<<<<< HEAD

    public Group getGroup() {
        return group;
=======
    public boolean isDefault() {
        return isDefault;
>>>>>>> 7fd570ae8f0a124fb2abb794e79fdc8b4ad45731
    }
}
