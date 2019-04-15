package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Category;

public class CategoryNameList implements Serializable {
    List<String> categories;
    public CategoryNameList(){
        categories = new ArrayList<>();
    }

    public List<String> getCategories() {
        return categories;
    }

    public void sort(){
        Collections.sort(categories);
    }
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (String category : categories) {
            names.add(category);
        }
        return names;
    }
    public boolean addCategoryName(String name){
        for (String c:categories) {
            if(c.equals(name)) return false;
        }
        categories.add(name);
        setChanges();
        return true;
    }

    private void setChanges(){
        Save.save(this);
    }
}