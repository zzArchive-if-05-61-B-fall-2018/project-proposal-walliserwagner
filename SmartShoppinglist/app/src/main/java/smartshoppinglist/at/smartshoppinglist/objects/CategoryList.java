package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.objects.Category;

public class CategoryList<T extends Comparable<T>> {
    List<Category<T>> categories;

    public CategoryList(){
        categories = new ArrayList<>();
    }

    public List<Category<T>> getCategories() {
        return categories;
    }
    public void addCategory(Category<T> category){
        categories.add(category);
    }
    public void sort(){
        Collections.sort(categories);
    }
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }
        return names;
    }
    public boolean addCategoryByName(Class<T> type, String name){
        for (Category c:categories) {
            if(c.getName().equals(name)) return false;
        }
        categories.add(new Category<T>(type,name,true));
        return true;
    }
}