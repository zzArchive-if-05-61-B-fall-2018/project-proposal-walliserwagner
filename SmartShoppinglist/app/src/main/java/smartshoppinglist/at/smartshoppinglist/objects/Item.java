package smartshoppinglist.at.smartshoppinglist.objects;

import java.io.Serializable;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;

public class Item implements Comparable<Item>, Serializable {
    private static int defaultCategory = -1;
    private static String defaultDefaultUnit = "Stk";
    private String name;
    private int icon;
    private int category;
    private String defaultUnit;

    public Item(String name, int icon, String category, String defaultUnit){
        this.name = name;
        this.icon = icon;
        this.category = MainActivity.getInstance().getItemCategorys().getCategoryIdByName(category);
        this.defaultUnit = defaultUnit;
    }
    public Item(String name, int icon, String category){
        this(name, icon, category, defaultDefaultUnit);
    }
    public Item(String name, String category){
        this(name, category, defaultDefaultUnit);
    }
    public Item(String name, String category,String defaultUnit){
        this.name = name;
        this.icon = getIconToName(name);
        this.category = MainActivity.getInstance().getItemCategorys().getCategoryIdByName(category);
        this.defaultUnit = defaultUnit;
    }
    public Item(String name){
        this(name,MainActivity.getInstance().getItemCategorys().getCategoryById(defaultCategory).getName());
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
        return MainActivity.getInstance().getItemCategorys().getCategoryById(category).getName();
    }
    public int getCategoryID() {
        return category;
    }

    public void setDefaultunit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public void setCategoryID(int category) {
        this.category = category;
    }
    public void setCategory(String category) {
        if(category.equals("")){
            this.category = MainActivity.getInstance().getItemCategorys().getCategoryIdByName(MainActivity.getInstance().getString(R.string.general));
            return;
        }
        this.category = MainActivity.getInstance().getItemCategorys().getCategoryIdByName(category);
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
        this.icon = getIconToName(name);
    }

    @Override
    public int compareTo(Item i) {
        return name.compareTo(i.name);
    }

    public static String getDefaultCategory() {
        return MainActivity.getInstance().getItemCategorys().getCategoryById(defaultCategory).getName();
    }

    public static String getDefaultDefaultUnit() {
        return defaultDefaultUnit;
    }


    public static void setDefaultDefaultUnit(String defaultDefaultUnit) {
        Item.defaultDefaultUnit = defaultDefaultUnit;
    }

    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    private int getIconToName(String name){
        try {
            return R.drawable.class.getField(name.toLowerCase().substring(0,1)).getInt(null);
        }catch (Exception e){
            return R.drawable.ic_questionmark;
        }
    }

}
