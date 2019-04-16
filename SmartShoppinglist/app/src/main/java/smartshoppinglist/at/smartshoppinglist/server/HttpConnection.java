package smartshoppinglist.at.smartshoppinglist.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

import smartshoppinglist.at.smartshoppinglist.BuildConfig;

public class HttpConnection {

    String Serverurl; //= "http://10.0.0.4:3000";

    public HttpConnection(){
        try {
            Field field = Class.forName("smartshoppinglist.at.smartshoppinglist.BuildConfig").getDeclaredField("HOST_IP");
            Serverurl = (String) field.get(null);
            Serverurl = "http://"+Serverurl;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    protected String sendGet(String getRequest) throws Exception {
        String url = Serverurl+getRequest;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setConnectTimeout(2000);
        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

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

    protected void sendPost(String head, String payload) throws Exception {
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
        System.out.println("POST Response Code :  " + responseCode);
        System.out.println("POST Response Message : " + httpCon.getResponseMessage());
        if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httpCon.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();
            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST NOT WORKED");
        }
    }
}
