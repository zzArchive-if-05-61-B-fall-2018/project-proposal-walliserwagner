package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.objects.CategoryNameList;
import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;

public class Read {

    public static Context context;
    public static int id;

    public static ItemList readItemList(){
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"itemlist.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return new ItemList();
        }
        Gson gson = new Gson();
        ItemList list = gson.fromJson(jsonString, ItemList.class);
        return list;
    }

    public static GroupList readGroupList(){
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"grouplist.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return new GroupList();
        }
        Gson gson = new Gson();
        GroupList list = gson.fromJson(jsonString, GroupList.class);
        return list;
    }

    public static Shoppinglist readShoppinglist(String name){
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"shoppinglist"+name+".json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Shoppinglist list = gson.fromJson(jsonString, Shoppinglist.class);
        return list;
    }

    public static CategoryNameList readCategoryList(){
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"categorynamelist.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return new CategoryNameList();
        }
        Gson gson = new Gson();
        CategoryNameList list = gson.fromJson(jsonString, CategoryNameList.class);
        return list;
    }

    private static JSONArray read(String name, String file) throws IOException, JSONException {
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jobj = null;
        JSONArray arr;
        jobj = new JSONObject(jsonString);
        try {
            Gson gson = new Gson();
            gson.fromJson(jsonString, ItemList.class);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(jobj == null){
            return null;
        }
        arr = jobj.getJSONArray(name);
        return arr;
    }

    private static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        if (!fl.exists()) {
            fl.createNewFile();
        }
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
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
        ItemList itemList = new ItemList();
        JSONArray arr = read(shoppinglistName, id + "shoppinglist.json");
        JSONObject currentJobj = null;
        for (int i = 0; i < arr.length(); i++) {
            currentJobj = arr.getJSONObject(i);
            if(itemList.findItemByName(currentJobj.getString("title"))!= null) {
                ItemContainer item = new ItemContainer(itemList.findItemByName
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
