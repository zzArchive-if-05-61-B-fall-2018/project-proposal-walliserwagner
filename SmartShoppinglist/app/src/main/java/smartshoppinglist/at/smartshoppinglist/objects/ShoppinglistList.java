package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShoppinglistList {
    private static final ShoppinglistList instance = new ShoppinglistList();
    private List<Shoppinglist> shoppinglists;
    private Shoppinglist currentLIst;

    public Shoppinglist getCurrentList() {
        return currentLIst;
    }

    public void setCurrentLIst(Shoppinglist currentLIst) {
        this.currentLIst = currentLIst;
    }

    public static ShoppinglistList getInstance() {
        return instance;
    }

    private ShoppinglistList() {
        shoppinglists = new ArrayList<>();
    }
    public Shoppinglist findShoppinglistByName(String name){
        for (Shoppinglist shoppinglist:shoppinglists) {
            if (shoppinglist.getName().equals(name)){
                return shoppinglist;
            }
        }
        return null;
    }
    public void addShoppinglist(Shoppinglist shoppinglist){
        shoppinglists.add(shoppinglist);
    }
    public void removeShoppinglist(Shoppinglist shoppinglist){
        shoppinglists.remove(shoppinglist);
    }
    public Shoppinglist[] getShoppinglist(){
        return shoppinglists.toArray(new Shoppinglist[0]);
    }
}
