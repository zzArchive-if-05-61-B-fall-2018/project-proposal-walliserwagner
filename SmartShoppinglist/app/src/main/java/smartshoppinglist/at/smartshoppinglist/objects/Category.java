package smartshoppinglist.at.smartshoppinglist.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category<T extends Comparable<T>> implements Comparable<Category<T>> {
    private static int defaultpriority = 0;
    private Class<T> type;
    private List<T> categrorizedObjects;
    private String name;
    private int priority;
    private boolean isExpanded = false;

    public Category(Class<T> type, String name,int priority){
        this.type = type;
        this.name = name;
        this.priority = priority;
        categrorizedObjects = new ArrayList<T>();
    }
    public Category(Class<T> type, String name){
        this(type,name,defaultpriority);
    }

    public Category(Class<T> type, String name, int priority,boolean expanded){
        this(type,name,priority);
        isExpanded = expanded;
    }
    public Category(Class<T> type, String name,boolean expanded){
        this(type,name,defaultpriority, expanded);
    }

    public void addElement(T element){
        categrorizedObjects.add(element);
    }
    public <T> T[] getElements(){
        T[] array = (T[]) Array.newInstance(type,categrorizedObjects.size());
        return categrorizedObjects.toArray(array);
    }
    public void removeElement(T element){
        categrorizedObjects.remove(element);
    }

    public String getName() {
        return name;
    }

    public void sort(){
        Collections.sort(categrorizedObjects);
    }

    public Class<T> getType() {
        return type;
    }
    public boolean containsElement(T element){
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
    public int compareTo(Category<T> c) {
        return ((Integer)c.getPriority()).compareTo(priority);
    }
}
