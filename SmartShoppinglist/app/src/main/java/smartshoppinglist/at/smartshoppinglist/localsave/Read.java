package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.InviteList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemCategoryList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.objects.RecipeList;
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

    public static InviteList readInviteList(){
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"invitelist.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return new InviteList();
        }
        Gson gson = new Gson();
        InviteList list = gson.fromJson(jsonString, InviteList.class);
        return list;
    }

    public static RecipeList readRecipeList(){
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"recipelist.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return new RecipeList();
        }
        Gson gson = new Gson();
        RecipeList list = gson.fromJson(jsonString, RecipeList.class);
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

    public static List<Shoppinglist> readShoppinglist(int groupId){
        String jsonString = null;
        File folder = new File(context.getFilesDir().getPath().toString());
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.startsWith(id+"shoppinglist") && name.endsWith("_"+groupId+".json")){
                    return true;
                }
                return false;
            }
        });
        List<Shoppinglist> shoppinglists = new ArrayList<>();
        Gson gson = new Gson();
        try {
            for (int i = 0; i < files.length; i++) {
                jsonString = getStringFromFile(files[i].getAbsolutePath());
                shoppinglists.add(gson.fromJson(jsonString, Shoppinglist.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoppinglists;
    }

    public static ItemCategoryList readCategoryList(){
        String jsonString = null;
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"categorynamelist.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return new ItemCategoryList();
        }
        Gson gson = new Gson();
        ItemCategoryList list = gson.fromJson(jsonString, ItemCategoryList.class);
        return list;
    }
    public static Config readConfig(){
        String jsonString = "";
        try {
            jsonString = getStringFromFile(context.getFilesDir().getPath().toString()+"/"+id+"config.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return Config.getInstance();
        }
        Gson gson = new Gson();
        Config config = gson.fromJson(jsonString, Config.class);
        return config;
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
}
