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
import android.widget.TextView;
import android.widget.Toast;

import com.project.yousof.androidapp.configuration.Configuration;
import com.project.yousof.androidapp.isconnected.IsConnected;
import com.project.yousof.androidapp.library.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Points extends Activity {

    Button home;
    TextView ch1points, ch2points;
    String ch1points_str,ch2points_str;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_points = "points";
    private static final String TAG_chpoints = "chpoints";

    private static final String TAG_points2 = "points2";
    private static final String TAG_chpoints2 = "chpoints2";


    private static String url = Configuration.SERVER_IP + "getpoints.php";

    private static String url2 = Configuration.SERVER_IP + "getpoints2.php";


    private JSONParser jParser = new JSONParser();
    private JSONParser jParser2 = new JSONParser();
    private JSONArray pointsarray = null;
    private JSONArray pointsarray2 = null;

    private ProgressDialog pDialog;

    private IsConnected isConnected;
    boolean ConnStatus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.points);


        home   =  (Button) findViewById(R.id.btnHome);
        ch1points = (TextView)findViewById(R.id.txtpointsch1);
        ch2points = (TextView)findViewById(R.id.txtpointsch2);

        pDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);


        ConnectAndGet();


        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent c = new Intent(getApplicationContext(), Main.class);
                startActivity(c);
            }

        });

    }

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
                            new Loadpoints().execute();
                        }
                    };
                    mHandler.postDelayed(mRunnable, 500);

                }else{
                    Toast.makeText(getApplicationContext(), "Fail to connect to server, please try again", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    public class Loadpoints extends AsyncTask<String, String, String> {

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
            Log.d("newUrl: ", url);
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);
            Log.d("++ Ahmad points details ++ ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1 ){
                    pointsarray = json.getJSONArray(TAG_points);
                    //Log.d("++ All chores details ++ ", chore.toString());

                    for (int i = 0; i < pointsarray.length(); i++) {
                        JSONObject c = pointsarray.getJSONObject(0);

                        ch1points_str = c.getString(TAG_chpoints);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //
            Log.d("newUrl: ", url2);
            JSONObject json2 = jParser2.makeHttpRequest(url2, "POST", params);
            Log.d("++ Mohammad points details ++ ", json2.toString());

            try {
                int success = json2.getInt(TAG_SUCCESS);

                if (success == 1 ){
                    pointsarray2 = json2.getJSONArray(TAG_points2);
                    //Log.d("++ All chores details ++ ", chore.toString());

                    for (int i = 0; i < pointsarray2.length(); i++) {
                        JSONObject c = pointsarray2.getJSONObject(0);

                        ch2points_str = c.getString(TAG_chpoints2);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



            //
            return null;
        }

        protected void onPostExecute(String file_url) {
            try{

                pDialog.dismiss();

                ch1points.setText(ch1points_str + "  point(s)");
                ch2points.setText(ch2points_str + "  point(s)");

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



}
