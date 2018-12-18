package smartshoppinglist.at.smartshoppinglist;

public class Item {
    private String name;
    private int icon;
    private String description;
    private String defaultUnit;

    public Item(String name, int icon, String description, String defaultUnit){
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.defaultUnit = defaultUnit;
    }
    public Item(String name, int icon, String description){
        this(name, icon, description, "Stk");
    }
    public Item(String name, int icon){
        this(name, icon,"");
    }

    public  Item(String name, String description){
        this(name, R.drawable.ic_questionmark ,description);
    }
    public Item(String name){
        this(name,"");
    }

    public int getIcon() {
        return icon;
    }
    public String getName(){
        return name;
    }

    public String getDefaultunit() {
        return defaultUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDefaultunit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }
}
