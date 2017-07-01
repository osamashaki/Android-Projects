package com.project.yousof.androidapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.yousof.androidapp.configuration.Configuration;
import com.project.yousof.androidapp.isconnected.IsConnected;
import com.project.yousof.androidapp.library.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Allowance extends Activity {

    Button home,add;
    Spinner chname;
    EditText allow;
    TextView error_msg;
    String msg;

    private static final String TAG_SUCCESS = "success";
    private static String url = Configuration.SERVER_IP + "adminlogin/addallowance.php";
    private JSONParser jParser = new JSONParser();

    private ProgressDialog pDialog;
    private IsConnected isConnected;
    boolean ConnStatus = false;
    String a;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addallowence);

        add = (Button)findViewById(R.id.btnadd);
        home   =  (Button) findViewById(R.id.btnHome);

        pDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);

        chname = (Spinner) findViewById(R.id.txtchild);
        allow = (EditText)findViewById(R.id.txtallowance);
        error_msg = (TextView)findViewById(R.id.add_error);


        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {


                if (!allow.getText().toString().equals("")) {

                    ConnectAndAdd();
                    Intent i = new Intent(getApplicationContext(), Main.class);
                    startActivity(i);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter allowance value", Toast.LENGTH_LONG).show();

                }

            }

        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent c = new Intent(getApplicationContext(), Main.class);
                startActivity(c);
            }

        });



    }
    private void ConnectAndAdd(){
        isConnected = new IsConnected(getApplicationContext());
        isConnected.SkipStrict();

        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog.setMessage("Checking Connection. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                    }
                });
                pDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... p){
                ConnStatus = isConnected.isConnectedToServer(Configuration.SERVER_IP + url);
                return ConnStatus;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                if (result){
                    Runnable mRunnable;
                    Handler mHandler = new Handler();
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            new Load().execute();
                        }
                    };
                    mHandler.postDelayed(mRunnable, 500);

                }else{
                    Toast.makeText(getApplicationContext(), "Fail to connect to server, please try again", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    public class Load extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading . Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String ch = chname.getSelectedItem().toString();
            String a = allow.getText().toString();



                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("chname", ch));
                params.add(new BasicNameValuePair("allowance", a));

                Log.d("newUrl: ", url);
                JSONObject json = jParser.makeHttpRequest(url, "POST", params);
                Log.d("++ Add Allowance ++ ", json.toString());

                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {

                        //error_msg.setText("Allowance added successfully");
                        pDialog.dismiss();

                        msg = "Allowance added successfully";


                    } else {
                        pDialog.dismiss();

                        msg = "Error in add allowance";

                        //error_msg.setText("Error in add allowance");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            return msg;
        }

        protected void onPostExecute(String msg) {
            try{
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


            } catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
    //end of get points method

    /*
    private void Add(){

        String ch = chname.getSelectedItem().toString();
        String a  = allow.getText().toString();

        if (!a.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please enter allowance value", Toast.LENGTH_LONG).show();

        }
        else {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("chname", ch));
            params.add(new BasicNameValuePair("allowance", a));

            Log.d("newUrl: ", url);
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
            Log.d("++ Add allowance ++ ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    error_msg.setText("Allowance added successfully");


                } else {
                    error_msg.setText("Error in add allowance");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    */


}
