package smartshoppinglist.at.smartshoppinglist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}