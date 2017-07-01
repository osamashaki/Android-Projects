package com.project.yousof.androidapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.yousof.androidapp.library.DatabaseHandler;
import com.project.yousof.androidapp.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ChangePassword extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    String sid;


    EditText newpass;
    TextView alert;
    Button changepass;
    Button cancel;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.project.yousof.androidapp.R.layout.changepassword);
        sid = getIntent().getStringExtra("sid");


        cancel = (Button) findViewById(com.project.yousof.androidapp.R.id.btcancel);
        cancel.setOnClickListener(new View.OnClickListener(){
        public void onClick(View arg0){

                Intent login = new Intent(getApplicationContext(), Main.class);
                startActivity(login);
                finish();
            }

        });



        newpass = (EditText) findViewById(com.project.yousof.androidapp.R.id.newpass);
        alert = (TextView) findViewById(com.project.yousof.androidapp.R.id.alertpass);
        changepass = (Button) findViewById(com.project.yousof.androidapp.R.id.btchangepass);

        //Toast.makeText(getApplicationContext(), "id: " + sid, Toast.LENGTH_LONG).show();



        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!newpass.getText().toString().equals("")) {
                    NetAsync(view);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    //
    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(ChangePassword.this);
            nDialog.setMessage("Loading..");
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
                    //URL url = new URL("http://www.google.com");//test internet using google.com
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
                alert.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String newpas,userid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();//araylist to  store data
            user = db.getUserDetails();// bring user details from sql lite db

            newpas = newpass.getText().toString();
            userid = sid;

            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.chgPass(newpas, userid);
            Log.d("Button", "Register");
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                        alert.setText("Your Password is successfully changed.");


                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        alert.setText("User not exist.");
                    } else {
                        pDialog.dismiss();
                        alert.setText("Error occured in changing Password.");
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
