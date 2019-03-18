package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

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

import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;

public class Save {

    public static Context context;


    public static synchronized void saveItemContainer(ItemContainer itemContainer) throws IOException, JSONException {
        save(new String[]{"title", "amount", "unit", "category", "ticked"},"items", itemContainer.getItem().getName(), itemContainer.getCount(), itemContainer.getUnit(), itemContainer.getItem().getCategory(), itemContainer.isTicked());
    }

    private static synchronized void save(String[] keys, String arrName, Object... values) throws IOException, JSONException {

        File fileJson = new File(context.getFilesDir().getAbsolutePath(),"db.json");
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
