package com.proj2.yousof.androidapp;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.proj2.yousof.androidapp.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class PasswordReset extends Activity {

private static String KEY_SUCCESS = "success";
private static String KEY_ERROR = "error";

  EditText email;
  TextView alert;
  Button resetpass;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  
        setContentView(R.layout.passwordreset);

        Button login = (Button) findViewById(R.id.bktolog);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });


        email = (EditText) findViewById(R.id.email);
        alert = (TextView) findViewById(R.id.alert);
        resetpass = (Button) findViewById(R.id.respass);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                NetAsync(view);

            }



        });}

    private class NetCheck extends AsyncTask<String,String,Boolean>

                {
                    private ProgressDialog nDialog;

                    @Override
                    protected void onPreExecute(){
                        super.onPreExecute();
                        nDialog = new ProgressDialog(PasswordReset.this);
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
                                //URL url = new URL("http://www.google.com");
                                URL url = new URL("http://10.0.2.2"); //checks for working internet connection by trying Google.

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

                    String emailtxt;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        emailtxt = email.getText().toString();
                        //Toast.makeText(getApplicationContext(), "Userid:" + emailtxt , Toast.LENGTH_SHORT).show();


                        pDialog = new ProgressDialog(PasswordReset.this);
                        pDialog.setTitle("Contacting Servers");
                        pDialog.setMessage("Getting Data ...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(true);
                        pDialog.show();
                    }

                    @Override
                    protected JSONObject doInBackground(String... args) {


                        UserFunctions userFunction = new UserFunctions();
                        JSONObject json = userFunction.forPass(emailtxt);
                        return json;


                    }


                    @Override
                    protected void onPostExecute(JSONObject json) {
                  /**
                   * Checks if the Password Change Process is sucesss
                   **/
                        try {
                            if (json.getString(KEY_SUCCESS) != null) {
                                alert.setText("");
                                String res = json.getString(KEY_SUCCESS);
                                String red = json.getString(KEY_ERROR);


                                if(Integer.parseInt(res) == 1){
                                   pDialog.dismiss();
                                    alert.setText("The new password is: 123456");



                                }
                                else if (Integer.parseInt(red) == 2)
                                {    pDialog.dismiss();
                                    alert.setText("Your email does not exist in our database.");
                                }
                                else {
                                    pDialog.dismiss();
                                    alert.setText("Error occured in password reset ");
                                }




                            }}
                        catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }}
            public void NetAsync(View view){
                new NetCheck().execute();
            }}





