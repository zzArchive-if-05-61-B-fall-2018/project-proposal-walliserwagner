package smartshoppinglist.at.smartshoppinglist.objects;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class Shoppinglist implements Comparable<Shoppinglist>, Serializable {
    @Expose
    private List<Category> items;

    @Expose private int groupId;

    @Expose private String name;
    private static String categoryBought = "Gekauft";

    private static String categoryGeneral = "Allgemein";

    @Expose private boolean isDefault = false;

    public Shoppinglist(String name, Group group) {
        this(name);
        this.groupId = group.getId();
    }

    public Shoppinglist(String name, Group group, boolean isDefault) {
        this(name, isDefault);
        this.groupId = group.getId();
    }

    public Shoppinglist(String name){
        this.name = name;
        items = new ArrayList<Category>();
        addCategory(new Category(categoryGeneral,-1,true));
        addCategory(new Category(categoryBought,-2,true));
    }

    public Shoppinglist(String name, boolean isDefault){
        this(name);
        this.isDefault = isDefault;
    }

    public void setName(String name) {
        Save.remove(this.name,groupId);
        this.name = name;
        setChanges();
    }

    public void addItemList(List<ItemContainer> itemContainers){
        for (ItemContainer itemContainer:
             itemContainers) {
            Category category = null;
            if(itemContainer.isTicked()){
                category = getCategoryByName(categoryBought);

            }else {
                category = getCategoryByName(itemContainer.getItem().getCategory());
            }
            if(category == null){
                category = new Category(itemContainer.getItem().getCategory(),true);
                addCategory(category);
            }
            category.addElement(itemContainer);
            category.sort();
        }
    }

    public Category[] getItems(){
        List<Category> result = new ArrayList<>();
        for (Category category:items) {
            if (category.getElements().length > 0){
                result.add(category);
            }
        }
        return result.toArray((Category[])Array.newInstance(Category.class,result.size()));
    }
    public void addCategory(Category c){
        items.add(c);
        sort();
    }

    public String getName() {
        return name;
    }

    public void addItem(ItemContainer itemContainer){
        Category category = getCategoryByName(itemContainer.getItem().getCategory());
        if(category == null){
            category = new Category(itemContainer.getItem().getCategory(),true);
            addCategory(category);
        }
        addExistingItems(itemContainer);
        category.addElement(itemContainer);
        category.sort();
        //Server.getInstance().postRequest("/shoppinglist", String.format(""));
        setChanges();
    }

    protected void setChanges(){
        Save.save(this);
    }
    public void removeItem(ItemContainer itemContainer){
        Category category;
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
    private Category getCategoryByName(String name){
        for (Category category:items) {
            if (category.getName().equals(name)){
                return  category;
            }
        }
        return null;
    }
    public void tickItem(ItemContainer itemContainer){
        itemContainer.setTicked(true);
        Category category = getCategoryByName(itemContainer.getItem().getCategory());
        if (category != null && category.containsElement(itemContainer)){
            category.removeElement(itemContainer);
            getCategoryByName(categoryBought).addElement(itemContainer);
            getCategoryByName(categoryBought).sort();
        }
        setChanges();
    }
    public void unTickItem(ItemContainer itemContainer){
        itemContainer.setTicked(false);
        Category category = getCategoryByName(categoryBought);
        if (category != null && category.containsElement(itemContainer)){
            category.removeElement(itemContainer);
            addItem(itemContainer);
        }
        setChanges();
    }
    public void removeTickedItems(){
        Category category = getCategoryByName(categoryBought);
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
        for (Category category: items) {
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
        Category category = getCategoryByName(itemContainer.getItem().getCategory());
        for (int i = 0; category.getElements().length > i; i++) {
            ItemContainer ic = (ItemContainer) category.getElements()[i];
            if(ic.getItem().getName().equals(itemContainer.getItem().getName()) && ic.getUnit().equals(itemContainer.getUnit()) && !ic.isTicked()){
                itemContainer.setCount(ic.getCount()+itemContainer.getCount());
                category.removeElement(ic);
                return;
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

    public Group getGroup() {
            return MainActivity.getInstance().getGroups().findGroupById(groupId);
    }
    public int getGroupId(){
        return groupId;
    }
    public boolean isDefault() {
        return isDefault;
    }

    public void setCategoryExpandedByName(String name, boolean expanded){
        Category category = getCategoryByName(name);
        if(category != null){
            category.setExpanded(expanded);
            setChanges();
        }
    }

    @Override
    public int compareTo(Shoppinglist o) {
        if(this.isDefault && !o.isDefault) return -1;
        else if(!this.isDefault && o.isDefault) return 1;
        return this.name.compareTo(o.name);
    }


    @Override
    public String toString() {
        return String.format("%s:%s",MainActivity.getInstance().getGroups().findGroupById(groupId),name);
    }
}
