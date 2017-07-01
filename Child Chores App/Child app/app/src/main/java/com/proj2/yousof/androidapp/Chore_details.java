package com.proj2.yousof.androidapp;

import android.app.Activity;
import android.app.AlertDialog;
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


public class Chore_details extends Activity implements View.OnClickListener {

    private static String url = "getchoredetails.php";
    private static String url_confirm = "confirmchore.php";

    private static final String TAG_chore = "chore";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESC = "description";
    private static final String TAG_CHILDID = "child_id";
    private static final String TAG_CHDATE = "choredate";
    private static final String TAG_STATUS = "status";


    private JSONParser jParser = new JSONParser();
    private JSONArray CHORE = null;
    private ProgressDialog pDialog;

    private IsConnected isConnected;
    boolean ConnStatus = false;

    TextView txtchore_title, txtchore_desc, txtchore_date,  txtchild,txtchore_status, alert;
    String chid = "0", choretstr, chore_descstr, chore_datestr,chorestatus, chorechildstr, errorMsg;
    Button btnHome, btnDel;

    String uid;

    public Chore_details() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chore_details);

        chid = getIntent().getStringExtra("chid");
        uid = getIntent().getStringExtra("sid");


        Init();

        ConnectAndGet();

    }

    private void Init() {

        pDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);


        txtchore_title = (TextView) findViewById(R.id.txtchoreTitle);
        txtchore_desc = (TextView) findViewById(R.id.txtchore_desc);
        txtchild = (TextView) findViewById(R.id.txtchild);
        txtchore_date = (TextView) findViewById(R.id.txtchore_date);
        txtchore_status = (TextView) findViewById(R.id.txtchore_status);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

        btnDel = (Button) findViewById(R.id.btnok);
        btnDel.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnHome:
                Intent next = new Intent(getApplicationContext(), Main.class);
                next.putExtra("sid",uid);

                startActivity(next);
                finish();
                break;

            case R.id.btnok:
                showAlert();
                break;

        }

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
                            new LoadChoreDetails().execute();
                        }
                    };
                    mHandler.postDelayed(mRunnable, 500);

                }else{
                    Toast.makeText(getApplicationContext(), "Fail to connect to server, please try again", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    public class LoadChoreDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading chore details. Please wait...");
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
            params.add(new BasicNameValuePair(TAG_ID, chid));

            Log.d("newUrl: ", Configuration.SERVER_IP + url);
            JSONObject json = jParser.makeHttpRequest(Configuration.SERVER_IP + url, "POST", params);
            Log.d("++ All chore details ++ ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1 ){
                    CHORE = json.getJSONArray(TAG_chore);

                    for (int i = 0; i < CHORE.length(); i++) {
                        JSONObject c = CHORE.getJSONObject(0);

                        choretstr = c.getString(TAG_TITLE);
                        chore_descstr = c.getString(TAG_DESC);
                        chorechildstr = c.getString(TAG_CHILDID);
                        chore_datestr = c.getString(TAG_CHDATE);
                        chorestatus = c.getString(TAG_STATUS);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            try{
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                txtchore_title.setText(choretstr);
                txtchore_desc.setText(chore_descstr);
                txtchild.setText(chorechildstr);
                txtchore_date.setText(chore_datestr);
                txtchore_status.setText(chorestatus);

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    public class Confirm extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Deleting Subject. Please wait...");
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
            runOnUiThread(new Runnable() {
                public void run() {


                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair(TAG_ID, chid));
                    params.add(new BasicNameValuePair("uid", uid));

                    Log.d("newUrl: ", Configuration.SERVER_IP + url_confirm);
                    JSONObject json = jParser.makeHttpRequest(Configuration.SERVER_IP + url_confirm, "POST", params);
                    Log.d("++ Confirm a chore ++ ", json.toString());

                    try {
                        int success = json.getInt(TAG_SUCCESS);
                        if (success != 1) {
                            Toast.makeText(getApplicationContext(), "You've successfully done this chore", Toast.LENGTH_LONG).show();
                            Intent next = new Intent(getApplicationContext(), Checkchore.class);
                            next.putExtra("sid",uid);
                            startActivity(next);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        protected void onPostExecute(String file_url) {
            try{
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }//

    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Chore_details.this);

        alertDialog.setTitle("Please confirm:");
        alertDialog.setMessage("Are you sure you have done this chore?");
        alertDialog.setIcon(R.drawable.red);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                new Confirm().execute();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.show();

    }







}