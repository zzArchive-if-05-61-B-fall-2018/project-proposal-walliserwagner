package smartshoppinglist.at.smartshoppinglist.server;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import smartshoppinglist.at.smartshoppinglist.objects.User;

public class Server {

    private static Server instance;
    String hostip;
    HttpConnection http;

    public static Server getInstance(){
        if(instance == null){
            try {
                instance = new Server();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
       http = new HttpConnection();
    }

    private boolean checkServerConnectivity(){
        try {
            return Inet4Address.getByName(hostip).isReachable(2000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User login(String email, String password){

        User result = null;
        try {
            String hashpw = hashPassword(password);
            ExecutorService exc = Executors.newSingleThreadExecutor();
            Callable<User> call = new Callable<User>() {
                @Override
                public User call() throws Exception {
                    String tmp = http.sendGet("/users?Email="+email+"&password="+password);
                    if(tmp.equals("")){
                        return null;
                    }

                    JSONArray jsonArray = new JSONArray(tmp);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    User user = new User(jsonObject.getString("Name"),
                    jsonObject.getString("Email"),
                    jsonObject.getInt("id"));
                    return user;
                }
            };
            Future<User> validLogin = exc.submit(call);
            result = validLogin.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean register(String email, String password, String name){

        try {
            ExecutorService exc = Executors.newSingleThreadExecutor();
            Callable<Boolean> call = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if(http.sendGet("/users?Email="+email).equals("")){
                        String hashpw = hashPassword(password);
                        http.sendPost("/users", String.format("{\"Name\":\"%s\",\"Email\":\"%s\",\"password\":\"%s\"}", name, email, password));
                        return true;
                    }
                    return false;
                }
            };
            Future<Boolean> valid = exc.submit(call);

            return valid.get(1, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private String hashPassword(String pw){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(pw.getBytes());
        String passwordHash = new String(messageDigest.digest());
        return passwordHash;
    }
}
