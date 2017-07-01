package com.proj2.yousof.androidapp;

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
import android.widget.TextView;
import android.widget.Toast;

import com.proj2.yousof.androidapp.configuration.Configuration;
import com.proj2.yousof.androidapp.isconnected.IsConnected;
import com.proj2.yousof.androidapp.library.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Allowance extends Activity {

    Button home;
    String sid;

    TextView ch1allowance;
    String allowance;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_allowance = "allowance";
    private static final String TAG_challowance = "challowance";

    private static String url = Configuration.SERVER_IP + "getchildallowance.php";
    private JSONParser jParser = new JSONParser();
    private JSONArray allowancearray = null;

    private ProgressDialog pDialog;
    private IsConnected isConnected;
    boolean ConnStatus = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.checkallowance);

        sid = getIntent().getStringExtra("sid");

        home   =  (Button) findViewById(R.id.btnHome);

        ch1allowance = (TextView)findViewById(R.id.txtchildallowance);

        pDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);

        ConnectAndGet();

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent c = new Intent(getApplicationContext(), Main.class);
                c.putExtra("sid",sid);
                startActivity(c);
            }

        });



    }

    //
    private void ConnectAndGet(){

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
                            new Loadallowance().execute();
                        }
                    };
                    mHandler.postDelayed(mRunnable, 500);

                }else{
                    Toast.makeText(getApplicationContext(), "Fail to connect to server, please try again", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    public class Loadallowance extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading points details. Please wait...");
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

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cid", sid));

            //Log.d("newUrl: ", url);
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
            Log.d("++ allowance details ++ ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1 ){
                    allowancearray = json.getJSONArray(TAG_allowance);
                    //Log.d("++ All chores details ++ ", chore.toString());

                    for (int i = 0; i < allowancearray.length(); i++) {
                        JSONObject c = allowancearray.getJSONObject(0);

                        allowance = c.getString(TAG_challowance);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            try{

                pDialog.dismiss();

                ch1allowance.setText(allowance + "  USD");

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
    //end of load points method



}
