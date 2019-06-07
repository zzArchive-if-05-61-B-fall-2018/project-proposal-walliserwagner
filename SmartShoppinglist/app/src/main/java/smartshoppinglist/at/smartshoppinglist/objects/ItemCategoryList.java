package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class ItemCategoryList implements Serializable {
    private Integer counter;
    private List<ItemCategory> categories;
    public ItemCategoryList(){
        categories = new ArrayList<>();
        getDefaultCategory();
        getCategoryBought();
    }

    public List<ItemCategory> getCategories() {
        List<ItemCategory> tmp = categories;
        tmp.remove(getCategoryBought());
        return tmp;
    }

    public void sort(){
        Collections.sort(categories);
        setChanges();
    }
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (ItemCategory category : categories) {
            if(category.getId() != -2) names.add(category.getName());
        }
        return names;
    }
    public boolean addCategoryName(String name, boolean defaultCategory){
        if(counter == null) counter = 0;
        ItemCategory bought = getCategoryBought();
        for (ItemCategory c:categories) {
            if(c.getName().equals(name)) return false;
        }
        for (ItemCategory c:categories) {
            if(c != bought) c.setPriority(c.getPriority()+1);
        }
        if(defaultCategory){
            ItemCategory itemCategory = getDefaultCategory();
            itemCategory.setName(name);
        }
        else {
            categories.add(new ItemCategory(++counter,name,0));
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

    public ItemCategory getCategoryById(int id){
        for (ItemCategory category: categories) {
            if(category.getId() == id){
                return category;
            }
        }
        return null;
    }
    public int getCategoryIdByName(String name){
        for (ItemCategory category: categories) {
            if(category.getName().equals(name)){
                return category.getId();
            }
        }
        addCategoryName(name);
        return getCategoryIdByName(name);
    }
    public ItemCategory getDefaultCategory(){
        for (ItemCategory category:categories) {
            if(category.isDefaultCategory()){
                return category;
            }
        }
        ItemCategory ic = new ItemCategory(-1, MainActivity.getInstance().getString(R.string.general),0,true);
        categories.add(ic);
        return ic;
    }
    public ItemCategory getCategoryBought(){
        for (ItemCategory category:categories) {
            if(category.getId() == -2){
                category.setName(MainActivity.getInstance().getString(R.string.bought));
                return category;
            }
        }
        ItemCategory ic = new ItemCategory(-2, MainActivity.getInstance().getString(R.string.bought),0);
        categories.add(ic);
        return ic;
    }

}