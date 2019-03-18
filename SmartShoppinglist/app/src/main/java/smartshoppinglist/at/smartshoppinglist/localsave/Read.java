package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;

public class Read {

    public static Context context;

    private static JSONArray read(String name) throws IOException, JSONException {
        FileReader fr = new FileReader(context.getFilesDir().getAbsolutePath()+"/db.json");
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


    public static List<ItemContainer> readItems() throws JSONException, IOException {
        List<ItemContainer> items = new LinkedList<>();
        ItemList itemList = ItemList.getInstance();
        JSONArray arr = read("items");
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
