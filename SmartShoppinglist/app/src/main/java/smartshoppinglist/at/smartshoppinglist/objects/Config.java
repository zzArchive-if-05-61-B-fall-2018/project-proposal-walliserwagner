package smartshoppinglist.at.smartshoppinglist.objects;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class Config {
    private static Config instance;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        setChanges();
    }

    public static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    private Config(){
        instance = this;
    }


    private Shoppinglist currentShoppinglist;

    public Shoppinglist getCurrentShoppinglist() {
        return currentShoppinglist;
    }

    public void setCurrentShoppinglist(Shoppinglist currentShoppinglist) {
        this.currentShoppinglist = currentShoppinglist;
        setChanges();
    }
    private void setChanges(){
        Save.save(instance);
    }
}
