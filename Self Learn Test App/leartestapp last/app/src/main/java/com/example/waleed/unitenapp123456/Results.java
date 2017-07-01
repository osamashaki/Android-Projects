package com.example.waleed.unitenapp123456;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.waleed.unitenapp123456.library.DatabaseHandler;
import com.example.waleed.unitenapp123456.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class Results extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    Button btn1;
    RadioButton a;
    RadioButton b;
    RadioButton c;
    TextView atxt;
    TextView btxt;
    TextView ctxt;
    TextView alert_txt;

    int pre_a=0,pre_b,pre_c;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        btn1 = (Button) findViewById(R.id.save);
        atxt = (TextView)findViewById(R.id.aresult);
        btxt = (TextView)findViewById(R.id.bresult);
        ctxt = (TextView)findViewById(R.id.cresult);
        alert_txt = (TextView) findViewById(R.id.alert);


        Bundle extras = getIntent().getExtras();
        pre_a = extras.getInt("ap");

        pre_b = extras.getInt("bp");

        pre_c = extras.getInt("cp");



        atxt.setText(String.valueOf(pre_a * 5));
        btxt.setText(String.valueOf(pre_b * 5));
        ctxt.setText(String.valueOf(pre_c * 5));

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NetAsync(view);

            }
        });






    }
    //


    //
    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Results.this);
            nDialog.setMessage("Please wait..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");//test internet using google.com
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                alert_txt.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String ares,bres,cres,useremail;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();//araylist to  store data
            user = db.getUserDetails();// bring user details from sql lite db

            ares = atxt.getText().toString();
            bres = btxt.getText().toString();
            cres = ctxt.getText().toString();
            useremail = user.get("email");

            //Toast.makeText(getApplicationContext(),"email: " + useremail,Toast.LENGTH_LONG ).show();


            pDialog = new ProgressDialog(Results.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.saveresult(ares, bres, cres, useremail);
            Log.d("Button", "Save results");
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert_txt.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                        alert_txt.setText("Your results saved successfully.");


                    } else if (Integer.parseInt(red) == 1) {
                        pDialog.dismiss();
                        alert_txt.setText("Error while saving results.");
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }
    }//

    public void NetAsync(View view){
        new NetCheck().execute();
    }

}
