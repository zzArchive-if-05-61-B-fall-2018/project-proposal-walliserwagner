package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;

public class Read {

    public static Context context;
    public static int id;

    private static JSONArray read(String name, String file) throws IOException, JSONException {
        FileReader fr = new FileReader(context.getFilesDir().getAbsolutePath() + "/" +file);
        BufferedReader br = new BufferedReader(fr);
        String currentLine = br.readLine();
        String jsonString = "";
        while(currentLine != null){
            jsonString += currentLine;
            currentLine = br.readLine();
        }
        JSONObject jobj = null;
        JSONArray arr;
        jobj = new JSONObject(jsonString);
        if(jobj == null){
            return null;
        }
        arr = jobj.getJSONArray(name);
        return arr;
    }

    public static HashMap<String, String> readConfig() throws IOException, JSONException {
        JSONArray arr = read("config", "config.json");
        int i = 0;
        JSONObject jobject;
        HashMap<String, String> config = new HashMap<>();
        while(arr.length() > 0 && arr.getJSONObject(i) != null){
            jobject = arr.getJSONObject(i);
            Iterator<String> it = jobject.keys();
            while (it.hasNext()){
                String tmp = it.next();
                config.put(tmp, (String)jobject.get(tmp));
            }
            i++;
        }
        return config;
    }

    public static List<Item> readItems() throws IOException, JSONException {
        List<Item> items = new LinkedList<>();
        JSONArray arr = read("items", id + "itemlist.json");
        JSONObject currentJobj = null;
        for (int i = 0; i < arr.length(); i++) {
            currentJobj = arr.getJSONObject(i);
            Item item = new Item(currentJobj.getString("name"),
                    currentJobj.getInt("icon"),
                    currentJobj.getString("category"),
                    currentJobj.getString("defaultUnit"));
            items.add(item);
        }
        return items;
    }

    public static List<ItemContainer> readShoppinglistItems(String shoppinglistName) throws JSONException, IOException {
        List<ItemContainer> items = new LinkedList<>();
        ItemList itemList = ItemList.getInstance();
        JSONArray arr = read(shoppinglistName, id + "shoppinglist.json");
        JSONObject currentJobj = null;
        for (int i = 0; i < arr.length(); i++) {
            currentJobj = arr.getJSONObject(i);
            if(itemList.FindItemByName(currentJobj.getString("title"))!= null) {
                ItemContainer item = new ItemContainer(itemList.FindItemByName
                        (currentJobj.getString("title")),
                        currentJobj.getInt("amount"),
                        currentJobj.getString("unit"));
                item.setTicked(currentJobj.getBoolean("ticked"));
                items.add(item);

            }
        }
        return items;
    }
}
