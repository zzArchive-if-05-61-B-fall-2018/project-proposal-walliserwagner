package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;

public class Save {

    public static Context context;

    public static synchronized void saveShoppinglist(Shoppinglist shoppinglist) throws IOException, JSONException {
        Category<ItemContainer>[] categories = shoppinglist.getItems();
        dumpJsonArray(shoppinglist.getName(), "shoppinglist.json");
        ItemContainer[] itemContainers;
        for (Category<ItemContainer> c:
             categories) {
            itemContainers = c.getElements();
            for (ItemContainer item:
                 itemContainers) {
                saveItemContainer(item, shoppinglist.getName());
            }
        }
    }

    public static synchronized void saveItemList(ItemList itemlist) throws JSONException, IOException {
        List<Item> items = itemlist.getItems();
        dumpJsonArray("items", "itemlist.json");
        for (Item item:
             items) {
            saveItem(item);
        }
    }

    private static synchronized void saveItem(Item item) throws IOException, JSONException {
        save("itemlist.json", new String[]{"name", "icon", "category", "defaultUnit"}, "items", item.getName(), item.getIcon(), item.getCategory(), item.getDefaultUnit());
    }

    private static synchronized void saveItemContainer(ItemContainer itemContainer, String shoppinglist) throws IOException, JSONException {
        save("shoppinglist.json", new String[]{"title", "amount", "unit", "category", "ticked"},shoppinglist, itemContainer.getItem().getName(), itemContainer.getCount(), itemContainer.getUnit(), itemContainer.getItem().getCategory(), itemContainer.isTicked());
    }

    private static void dumpJsonArray(String arrayName, String filename) throws JSONException {
        File fileJson = new File(context.getFilesDir().getAbsolutePath(),filename);
        JSONArray jsonArray = getJsonArrayfromFile(fileJson, arrayName);
        int i = 0;
        while(jsonArray.length() > 0 && jsonArray.getJSONObject(i) != null){
            jsonArray.remove(i);
        }

        JSONObject currentJsonObject = new JSONObject();
        currentJsonObject.put(arrayName,jsonArray);

        writeJsonFile(fileJson, currentJsonObject.toString());
    }

    private static synchronized void save(String filename, String[] keys, String arrName, Object... values) throws IOException, JSONException {

        File fileJson = new File(context.getFilesDir().getAbsolutePath(),filename);

        JSONArray jsonArray = getJsonArrayfromFile(fileJson, arrName);

        JSONObject jsonObj = new JSONObject();

        int i = 0;
        for (Object v:
             values) {
            jsonObj.put(keys[i], String.valueOf(v));
            i++;
        }
        jsonArray.put(jsonObj);

        JSONObject currentJsonObject = new JSONObject();
        currentJsonObject.put(arrName,jsonArray);

        writeJsonFile(fileJson, currentJsonObject.toString());
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

    private static JSONArray getJsonArrayfromFile(File fileJson, String arrName) throws JSONException {
        String strFileJson = null;
        try {
            strFileJson = getStringFromFile(fileJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject oldJsonObj;
        JSONArray jsonArray;
        if(!strFileJson.equals("")) {
            oldJsonObj = new JSONObject(strFileJson);
            jsonArray = oldJsonObj.getJSONArray(arrName);
        }
        else {

            jsonArray = new JSONArray();
        }
        return jsonArray;
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

    public static void writeJsonFile(File file, String json) {
        BufferedWriter bufferedWriter = null;
        try {

            if (!file.exists()) {
                Log.e("App","file not exist");
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
