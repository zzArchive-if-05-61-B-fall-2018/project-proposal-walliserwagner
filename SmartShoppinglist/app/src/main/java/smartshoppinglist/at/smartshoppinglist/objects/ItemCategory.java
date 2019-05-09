package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;

public class ItemCategory implements Serializable, Comparable<ItemCategory> {
    private String name;
    private int priority;
    private boolean defaultCategory;

    public ItemCategory(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public ItemCategory(String name, int priority, boolean defaultCategory) {
        this.name = name;
        this.priority = priority;
        this.defaultCategory = defaultCategory;
    }

    public boolean isDefaultCategory() {
        return defaultCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(ItemCategory c) {
        return Integer.compare(c.priority,priority);
    }
}
