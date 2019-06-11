package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;

public class ItemCategory implements Serializable, Comparable<ItemCategory> {
    private String name;
    private int priority;
    private boolean defaultCategory;
    private int id;

    public ItemCategory(int id, String name, int priority) {
        this.name = name;
        this.priority = priority;
        this.id = id;
    }

    public ItemCategory(int id, String name, int priority, boolean defaultCategory) {
        this.name = name;
        this.priority = priority;
        this.defaultCategory = defaultCategory;
        this.id = id;
    }

    public boolean isDefaultCategory() {
        return defaultCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        MainActivity.getInstance().getItemCategorys().setChanges();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        MainActivity.getInstance().getItemCategorys().setChanges();
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(ItemCategory c) {
        return Integer.compare(c.priority,priority);
    }
}
