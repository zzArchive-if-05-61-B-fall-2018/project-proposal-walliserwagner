package smartshoppinglist.at.smartshoppinglist.server;

import android.app.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.User;

public class Server {

    private static Server instance;

    static {
        try {
            instance = new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String hostip;
    HttpConnection http = new HttpConnection();

    public static Server getInstance(){
        return instance;
    }

    private Server() throws Exception{
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        try {
            Field field = Class.forName("smartshoppinglist.at.smartshoppinglist.BuildConfig").getDeclaredField("HOST_IP");
            hostip = (String) field.get(null);
            hostip = hostip.split("[:]")[0];
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public User login(String email, String password, Activity caller){

        User result = null;

        String hashpw = hashPassword(password);

        HttpRequest request = new HttpRequest(http, caller);
        request.execute("GET", "/users?email="+email+"&password="+hashpw);
        String jsonString = "";
        try {
            jsonString = request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return null;
        }

        JSONArray jsonArray = null;
        User user = null;
        try {
            jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            user = new User(jsonObject.getString("name"),
                    jsonObject.getString("email"),
                    jsonObject.getString("password"),
                    jsonObject.getInt("userid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String deleteRequest(String header){
        MainActivity.getInstance().reload();
        HttpRequest request = new HttpRequest(http, MainActivity.getInstance());
        request.execute("DELETE", header);

        String result = "";
        try {
            result = request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String postRequest(String header, String body){
        MainActivity.getInstance().reload();
        HttpRequest request = new HttpRequest(http, MainActivity.getInstance());
        request.execute("POST", header, body);

        String result = "";
        try {
            result = request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getRequest(String header){
        HttpRequest request = new HttpRequest(http, MainActivity.getInstance());
        request.execute("GET", header);

        String result = "";
        try {
            result = request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean userExists(String email, Activity caller){
        HttpRequest request = new HttpRequest(http, caller);
        request.execute("GET", "/users?email="+email);
        String result = "";
        try {
            result = request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return !result.equals("");
    }

    public boolean register(String email, String password, String name, Activity caller){

        if(userExists(email,caller)){
            return false;
        }
        HttpRequest request = new HttpRequest(http, caller);
        password = hashPassword(password);
        request.execute("POST", "/users", String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}", name, email, password));
        String jsonString = "";
        try {
            jsonString = request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(jsonString.equals("")){
            return false;
        }
        return true;
    }

    private String hashPassword(String pw){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(pw.getBytes());
        String passwordHash = convertStringToChars(new String(messageDigest.digest()));
        return passwordHash;
    }

    private String convertStringToChars(String input){
        String copy="";
        if(input.equals(""))return "";
        copy = String.valueOf((int)input.charAt(0));
        for (int i = 1; i < input.length(); i++) {
            copy += ";"+String.valueOf((int)input.charAt(i));
        }
        return copy;
    }

    public List<User> getUsersOfGroup(int groupid, Activity caller){
        HttpRequest request = new HttpRequest(http, caller);
        request.execute("GET", String.format("/group?groupid=%d",groupid));
        List<User> users = new ArrayList<>();
        try {
            String jstring = request.get();
            JSONArray array = new JSONArray(jstring);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                users.add(new User(jsonObject.getString("name"),jsonObject.getString("email"),"",jsonObject.getInt("userid")));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }

    public GroupList getGrouplist(GroupList groupList, Activity caller){
        HttpRequest request = new HttpRequest(http, caller);
        request.execute("GET", "/grouplist?userid="+MainActivity.getInstance().getCurrentUser().getId());
        try {
            JSONArray array = new JSONArray(request.get());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                if(groupList.findGroupById(jsonObject.getInt("groupid")) == null){
                    groupList.addGroup(new Group(jsonObject.getString("name"), MainActivity.getInstance().getCurrentUser(), jsonObject.getInt("groupid")));
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    public void getGroupChanges(Group group, Activity caller){
        HttpRequest request = new HttpRequest(http, caller);
        request.execute("GET", String.format("/changeset?groupid=%d&changeid=%d",group.getId(),group.getChangeset()+1));

        try {
            String tmp = request.get();
            JSONArray array = new JSONArray(tmp);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                String action = row.getString("action");
                JSONObject data = new JSONObject(row.getString("data").replaceAll("\\\\",""));
                if(action.equals("ADDITEM")){
                    Item item = new Item(data.getString("itemname"));
                    MainActivity.getInstance().getItems().addItem(item);
                    group.findListByName(data.getString("listname")).addItem(new ItemContainer(item, data.getInt("count"), data.getString("unit")),false);
                    group.incrementChangeset();
                }
                else if(action.equals("DELITEM")){
                    group.findListByName(data.getString("listname")).removeItem(data.getString("itemname"), data.getString("unit"));
                    group.incrementChangeset();
                }
                else if(action.equals("TICKITEM")){
                    group.findListByName(data.getString("listname")).itemChangeTick(group.findListByName(data.getString("listname")).findItemByNameAndUnit(data.getString("itemname"), data.getString("unit")), data.getBoolean("isticked"));
                    group.incrementChangeset();
                }
                else if(action.equals("ADDSHOPPINGLIST")){
                    group.addList(data.getString("listname"));
                    group.incrementChangeset();
                }
                else if(action.equals("DELSHOPPINGLIST")){
                    group.removeShoppinglist(data.getString("listname"));
                    group.incrementChangeset();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(Activity caller){
        return http.checkServerConnectivity();
    }
}
