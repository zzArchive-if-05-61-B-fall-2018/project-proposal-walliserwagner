package smartshoppinglist.at.smartshoppinglist.server;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

import smartshoppinglist.at.smartshoppinglist.BuildConfig;

public class HttpConnection {

    String Serverurl;
    String hostip;

    public HttpConnection(){
        try {
            Field field = Class.forName("smartshoppinglist.at.smartshoppinglist.BuildConfig").getDeclaredField("HOST_IP");
            hostip = (String) field.get(null);
            Serverurl = "http://"+hostip;
            hostip = hostip.split("[:]")[0];
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public boolean checkServerConnectivity(){
        try {
            //return Inet4Address.getByName(hostip).isReachable(100);
            return isReachable(hostip,3000,100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private static boolean isReachable(String addr, int openPort, int timeOutMillis) {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    protected String sendGet(String getRequest) throws Exception {

        String url = Serverurl+getRequest;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setConnectTimeout(2000);
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if(response.length() <= 2){
            return "";
        }

        return response.toString();
    }

    protected String sendDelete(String deleteRequest) throws Exception {

        String url = Serverurl+deleteRequest;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("DELETE");
        con.setConnectTimeout(2000);
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if(response.length() <= 2){
            return "";
        }

        return response.toString();
    }


    protected String sendPost(String head, String payload) throws Exception {

        URL url = new URL(Serverurl+head);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("POST");

        httpCon.setRequestProperty("Content-Type", "application/json");
        httpCon.setDoOutput(true);
        httpCon.setConnectTimeout(2000);
        OutputStream os = httpCon.getOutputStream();
        os.write(payload.getBytes());
        os.flush();
        os.close();
        int responseCode = httpCon.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpCon.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();
            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("POST NOT WORKED");
        }
        return "";
    }
}
