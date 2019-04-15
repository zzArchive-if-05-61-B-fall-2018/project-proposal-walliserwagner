package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.JsonWriter;
import android.util.Log;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.CategoryNameList;
import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;

public class Save {

    public static Context context;
    public static int id;

    public static void save(ItemList list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"itemlist.json"), str);
    }

    public static void save(Shoppinglist list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"shoppinglist"+list.getName()+".json"), str);
    }

    public static void save(GroupList list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"grouplist.json"), str);
    }

    public static void save(CategoryNameList list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"categorynamelist.json"), str);
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
