package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;

public class Category implements Comparable<Category>, Serializable {
    private transient static int defaultpriority = 0;
    private List<ItemContainer> categrorizedObjects;
    private int itemCategory;
    private int priority;
    private boolean isExpanded = false;

    public Category(String name,int priority){
        this.itemCategory = MainActivity.getInstance().getItemCategorys().getCategoryIdByName(name);
        this.priority = priority;
        categrorizedObjects = new ArrayList<ItemContainer>();
    }
    public Category(String name){
        this(name,defaultpriority);
    }

    public Category(String name, int priority,boolean expanded){
        this(name);
        this.priority = priority;
        isExpanded = expanded;
    }
    public Category(String name,boolean expanded){
        this(name,defaultpriority, expanded);
    }

    public void addElement(ItemContainer element){
        categrorizedObjects.add(element);
    }

    public ItemContainer[] getElements(){
        return categrorizedObjects.toArray(new ItemContainer[0]);
    }

    public void removeElement(ItemContainer element){
        categrorizedObjects.remove(element);
    }

    public String getName() {
        return  MainActivity.getInstance().getItemCategorys().getCategoryById(itemCategory).getName();
    }

    public void sort(){
        Collections.sort(categrorizedObjects);
    }

    public boolean containsElement(ItemContainer element){
        if (categrorizedObjects.contains(element)){
            return true;
        }
        return false;
    }
    public void clear(){
        categrorizedObjects.clear();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public static int getDefaultpriority() {
        return defaultpriority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Category c) {
        return ((Integer)c.getPriority()).compareTo(priority);
    }
}
