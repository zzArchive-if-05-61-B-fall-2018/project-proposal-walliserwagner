package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.InviteList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemCategoryList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.objects.RecipeList;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;

public class Save {

    public static Context context;
    public static int id;

    public static void save(ItemList list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"itemlist.json"), str);
    }

    public static void save(InviteList list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"invitelist.json"), str);
    }

    public static void save(RecipeList list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"recipelist.json"), str);
    }

    public static void save(Shoppinglist list){
        GsonBuilder gsonBuilder = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if(f.getName().equals("shoppinglists"))
                {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        Gson gson = gsonBuilder.create();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"shoppinglist"+list.getName()+"_"+list.getGroup().getId()+".json"), str);
    }

    public static void remove(String shoppinglistname, int groupId){
        File file = new File(context.getFilesDir().getPath().toString()+"/"+id+"shoppinglist"+shoppinglistname+"_"+groupId+".json");
        file.delete();
    }

    public static void save(GroupList list){
        GsonBuilder gsonBuilder = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if(f.getName().equals("shoppinglists"))
                {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        Gson gson = gsonBuilder.create();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"grouplist.json"), str);
    }

    public static void save(ItemCategoryList list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+id+"categorynamelist.json"), str);
    }
    public static void save(Config config){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String str = gson.toJson(config);
        writeJsonFile(new File(context.getFilesDir().getPath().toString()+"/"+"config.json"), str);
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
