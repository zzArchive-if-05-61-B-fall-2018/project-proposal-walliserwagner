package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Category;

public class ItemCategoryList implements Serializable {
    List<ItemCategory> categories;
    public ItemCategoryList(){
        categories = new ArrayList<>();
    }

    public List<ItemCategory> getCategories() {
        return categories;
    }

    public void sort(){
        Collections.sort(categories);
        setChanges();
    }
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (ItemCategory category : categories) {
            names.add(category.getName());
        }
        return names;
    }
    public boolean addCategoryName(String name, boolean defaultCategory){
        for (ItemCategory c:categories) {
            if(c.getName().equals(name)) return false;
        }
        for (ItemCategory c:categories) {
            if(!c.isDefaultCategory()) c.setPriority(c.getPriority()+1);
        }
        if(defaultCategory){
            categories.add(new ItemCategory(name,-1,true));
        }
        else {
            categories.add(new ItemCategory(name,0));
        }
        setChanges();
        return true;
    }
    public boolean addCategoryName(String name){
        return addCategoryName(name,false);
    }

    public Integer getPriorityByName(String name){
        for (ItemCategory c:categories) {
            if(c.getName().equals(name)) return c.getPriority();
        }
        return null;
    }

    private void setChanges(){
        Save.save(this);
    }


}