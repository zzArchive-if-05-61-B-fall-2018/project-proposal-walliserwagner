package smartshoppinglist.at.smartshoppinglist;

public class ItemContainer {
    private Item item;
    private int count;
    private String unit;

    public ItemContainer(Item item, int count, String unit){
        this.item = item;
        this.count = count;
        this.unit = unit;
    }
    public ItemContainer(Item item, int count){
        this(item, count, "Stk");
    }
    public ItemContainer(Item item, String unit){
        this(item, 1, unit);
    }
    public ItemContainer(Item item){
        this(item, 1);
    }

    public int getCount() {
        return count;
    }

    public Item getItem() {
        return item;
    }

    public String getUnit() {
        return unit;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
