package smartshoppinglist.at.smartshoppinglist.objects;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class Config {
    private static Config instance;

    private HashMap<String, String> config;

    public static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    private Config(){
        config = new HashMap<>();
        readConfig();
    }


    private void readConfig(){
        try {
            config = Read.readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Shoppinglist currentShoppinglist;

    public Shoppinglist getCurrentShoppinglist() {
        return currentShoppinglist;
    }

    public void setCurrentShoppinglist(Shoppinglist currentShoppinglist) {
        this.currentShoppinglist = currentShoppinglist;
        setChange();
    }

    private void setChange(){
        Save.saveConfig(config);
    }
}
