package com.project.yousof.androidapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Chore_details extends Activity implements View.OnClickListener {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";

    private static String url = "getchoredetails.php";
    private static String url_DELETE = "deletechore.php";


    private static final String TAG_chore = "chore";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESC = "description";
    private static final String TAG_CHILDID = "child_id";
    private static final String TAG_CHDATE = "choredate";
    private static final String TAG_STATUS = "status";

    private JSONParser jParser = new JSONParser();
    private JSONArray chore = null;
    private ProgressDialog pDialog;

    private IsConnected isConnected;
    boolean ConnStatus = false;

    TextView txtchore_title, txtchore_desc, txtchore_date,  txtchild,txtchore_status, alert;
    String chid = "0", choretstr, chore_descstr, chore_datestr,choresta, chorechildstr, errorMsg;

    Button btnHome, btndel;

    String st;

    public Chore_details() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chore_details);

        chid = getIntent().getStringExtra("chid");

        Init();


        ConnectAndGet();


    }

    private void Init() {

        pDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);

        txtchore_title = (TextView) findViewById(R.id.txtchoreTitle);
        txtchore_desc  = (TextView) findViewById(R.id.txtchore_desc);
        txtchild = (TextView) findViewById(R.id.txtchild);
        txtchore_date = (TextView) findViewById(R.id.txtchore_date);
        txtchore_status = (TextView) findViewById(R.id.txtchore_status);

        alert = (TextView) findViewById(R.id.alert);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

        btndel = (Button) findViewById(R.id.btnDel);
        btndel.setOnClickListener(this);


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnHome:
                Intent next = new Intent(getApplicationContext(), Main.class);
                startActivity(next);
                finish();
                break;

            case R.id.btnDel:
                showDeleteAlert();
                break;

            /*
            case R.id.update:
                new Updatestatus().execute();
                Intent i = new Intent(getApplicationContext(), Checkchore.class);
                Bundle translateBundle = ActivityOptions.makeCustomAnimation(Chore_details.this, R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
                startActivity(i, translateBundle);
                finish();
                break;
                */
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
            pDialog.setMessage("Loading chores details. Please wait...");
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
            Log.d("++ All chores details ++ ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1 ){
                    chore = json.getJSONArray(TAG_chore);
                    //Log.d("++ All chores details ++ ", chore.toString());

                    for (int i = 0; i < chore.length(); i++) {
                        JSONObject c = chore.getJSONObject(0);

                        choretstr = c.getString(TAG_TITLE);
                        chore_descstr = c.getString(TAG_DESC);
                        chore_datestr = c.getString(TAG_CHDATE);
                        chorechildstr = c.getString(TAG_CHILDID);
                        choresta = c.getString(TAG_STATUS);
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
                //if(chorechildstr == "1") {
                txtchild.setText(chorechildstr);
                //}
                //else if(chorechildstr == "2")
                  //  txtchild.setText("Mohammad");

                txtchore_date.setText(chore_datestr);
                txtchore_status.setText(choresta);



            } catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
    //end of get load chores method



    public class DelChore extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Deleting chore. Please wait...");
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

                    Log.d("newUrl: ", Configuration.SERVER_IP + url_DELETE);
                    JSONObject json = jParser.makeHttpRequest(Configuration.SERVER_IP + url_DELETE, "POST", params);
                    Log.d("++ Delete chore ++ ", json.toString());

                    try {
                        int success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            Toast.makeText(getApplicationContext(), "You've Successfully Delete a chore", Toast.LENGTH_LONG).show();
                            Intent next = new Intent(getApplicationContext(), Checkchore.class);
                            next.putExtra("fromDeletingchore", "fromDeletingchore");
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

    public void showDeleteAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Chore_details.this);

        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Are you sure you want delete this chore?");
        alertDialog.setIcon(R.drawable.delete);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                new DelChore().execute();
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