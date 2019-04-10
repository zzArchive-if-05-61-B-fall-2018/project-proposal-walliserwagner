package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;

import smartshoppinglist.at.smartshoppinglist.R;

public class Item implements Comparable<Item>, Serializable {
    private static String defaultCategory = "Allgemein";
    private static String defaultDefaultUnit = "Stk";
    private String name;
    private int icon;
    private String category;
    private String defaultUnit;

    public Item(String name, int icon, String category, String defaultUnit){
        this.name = name;
        this.icon = icon;
        this.category = category;
        this.defaultUnit = defaultUnit;
    }
    public Item(String name, int icon, String category){
        this(name, icon, category, defaultDefaultUnit);
    }
    public Item(String name, int icon){
        this(name, icon,defaultCategory);
    }

    public  Item(String name, String category){
        this(name, R.drawable.ic_questionmark , category);
    }
    public Item(String name){
        this(name,defaultCategory);
    }

    public int getIcon() {
        return icon;
    }
    public String getName(){
        return name;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public String getCategory() {
        return category;
    }

    public void setDefaultunit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public void setCategory(String category) {
        if(category.equals("")){
            this.category = defaultCategory;
        }
        else{
            this.category = category;
        }
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Item i) {
        return name.compareTo(i.name);
    }

    public static String getDefaultCategory() {
        return defaultCategory;
    }

    public static String getDefaultDefaultUnit() {
        return defaultDefaultUnit;
    }
}
