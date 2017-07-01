package com.project.yousof.androidapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.yousof.androidapp.configuration.Configuration;
import com.project.yousof.androidapp.helpermethods.EditTextValidator;
import com.project.yousof.androidapp.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Assign extends Activity implements View.OnClickListener {

    Button home,assign;
    EditText inputtitle,inputdesc;
    Spinner inputchname;
    TextView addErrorMsg;

    EditTextValidator edtTitleValidator,edtdescValidator;
    boolean emptyTitleEdttext = false;
    boolean emptydescEdttext = false;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String url = Configuration.SERVER_IP + "addchore.php";

    private static final String TAG_title = "title";
    private static final String TAG_desc  = "desc";
    private static final String TAG_name = "chname";

    private ProgressDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addchore);

        assign = (Button)findViewById(R.id.btnassign);
        home   =  (Button) findViewById(R.id.btnHome);

        inputtitle = (EditText)findViewById(R.id.choretitle);
        inputdesc = (EditText)findViewById(R.id.choredesc);
        inputchname = (Spinner)findViewById(R.id.child);
        addErrorMsg = (TextView)findViewById(R.id.add_error_msg);

        inputtitle.setOnClickListener(this);
        edtTitleValidator = new EditTextValidator(getApplicationContext(), inputtitle, "empty");

        inputdesc.setOnClickListener(this);
        edtdescValidator = new EditTextValidator(getApplicationContext(), inputdesc, "empty");



        assign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                edtTitleValidator.EditTextForCheck();
                emptyTitleEdttext = edtTitleValidator.empty;

                edtdescValidator.EditTextForCheck();
                emptydescEdttext = edtdescValidator.empty;

                if (!emptyTitleEdttext && !emptydescEdttext ){
                    NetAsync(view);
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

    @Override
    public void onClick(View v) {

    }

    private class NetCheck extends AsyncTask<String,String,Boolean>

    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Assign.this);
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
                    URL url = new URL("http://www.google.com"); //checks for working internet connection by trying Google.

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
                new AssignChore().execute();
            }
            else{
                nDialog.dismiss();
                addErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    private class AssignChore extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String title = inputtitle.getText().toString();
        String desc = inputdesc.getText().toString();
        String name = inputchname.getSelectedItem().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Assign.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.addchore(title, desc, name);
            return json;


        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks if the Password Change Process is sucesss
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    addErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if(Integer.parseInt(res) == 1) {
                        pDialog.dismiss();

                        Toast.makeText(getApplicationContext(),"Chore has been successfully added", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(), Checkchore.class);
                        Bundle translateBundle = ActivityOptions.makeCustomAnimation(Assign.this, R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
                        startActivity(i, translateBundle);
                        finish();

                    }

                    else if (Integer.parseInt(red) == 2)
                    {    pDialog.dismiss();
                        addErrorMsg.setText("Error in adding a chore.");
                    }
                    else {
                        pDialog.dismiss();
                        addErrorMsg.setText("Error occured in adding chore ");
                    }




                }}
            catch (JSONException e) {
                e.printStackTrace();


            }
        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }


}
