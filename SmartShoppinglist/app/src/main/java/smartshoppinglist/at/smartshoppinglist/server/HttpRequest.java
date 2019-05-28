package smartshoppinglist.at.smartshoppinglist.server;

import android.app.Activity;
import android.os.AsyncTask;
import android.app.ProgressDialog;

public class HttpRequest extends AsyncTask<String, Integer, String> {

    private HttpConnection http;
    private ProgressDialog progressDialog;

    public HttpRequest(HttpConnection httpConnection, Activity caller){
        http = httpConnection;
        progressDialog = new ProgressDialog(caller);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setTitle("Loading");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.cancel();
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {
            if (strings[0].equals("GET")) {
                result = http.sendGet(strings[1]);
            } else if (strings[0].equals("POST")) {
                result = http.sendPost(strings[1], strings[2]);
            }
            else if(strings[0].equals("DELETE")){
                result = http.sendDelete(strings[1]);
            }
            else if(strings[0].equals("CON")){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



}
