package com.example.waleed.unitenapp123456;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waleed.unitenapp123456.library.DatabaseHandler;
import com.example.waleed.unitenapp123456.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Activity {

    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    CheckBox remember;
    private TextView loginErrorMsg;
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.waleed.unitenapp123456.R.layout.login);

        inputEmail = (EditText) findViewById(com.example.waleed.unitenapp123456.R.id.email);
        inputPassword = (EditText) findViewById(com.example.waleed.unitenapp123456.R.id.pword);
        Btnregister = (Button) findViewById(com.example.waleed.unitenapp123456.R.id.registerbtn);
        btnLogin = (Button) findViewById(com.example.waleed.unitenapp123456.R.id.login);
        passreset = (Button) findViewById(com.example.waleed.unitenapp123456.R.id.passres);
        loginErrorMsg = (TextView) findViewById(com.example.waleed.unitenapp123456.R.id.loginErrorMsg);
        remember = (CheckBox) findViewById(R.id.rememberme) ;

        //for remember me check box
        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences test_email = getSharedPreferences("inputEmail", 0);
                SharedPreferences.Editor editor1 = test_email.edit();
                editor1.putString("inputEmail", inputEmail.getText().toString());

                SharedPreferences test_password = getSharedPreferences("inputPassword", 0);
                SharedPreferences.Editor editor2 = test_password.edit();
                editor2.putString("inputPassword", inputPassword.getText().toString());

                editor1.commit();
                editor2.commit();
            }
        });
        SharedPreferences test_email = getSharedPreferences("inputEmail", 0);
        inputEmail.setText(test_email.getString("inputEmail", ""));

        SharedPreferences test_password = getSharedPreferences("inputPassword", 0);
        inputPassword.setText(test_password.getString("inputPassword", ""));


        remember.setChecked(true);

        passreset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });

/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {

                String email = inputEmail.getText().toString();
                //loginErrorMsg.setText("e:" + email);
                if (isValidEmailAddress(email))
                {
                    if ((!inputEmail.getText().toString().equals("")) && (!inputPassword.getText().toString().equals("")))
                    {
                        NetAsync(view);
                    }
                    else if ((!inputEmail.getText().toString().equals(""))) {
                        Toast.makeText(getApplicationContext(),
                                "Password field incorrect", Toast.LENGTH_SHORT).show();
                    }
                    else if ((!inputPassword.getText().toString().equals("") )) {
                        Toast.makeText(getApplicationContext(),
                                "Email field incorrect", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                              "Email and Password field are incorrect", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    //Toast.makeText(getApplicationContext(),
                      //      "Invalid email address!", Toast.LENGTH_SHORT).show();
                    loginErrorMsg.setText("Invalid email address!");
                }

                /*

*/

            }
        });
    }


    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Login.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
        **/
        @Override
        protected Boolean doInBackground(String... args){



            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
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
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                loginErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String email,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputEmail = (EditText) findViewById(com.example.waleed.unitenapp123456.R.id.email);
            inputPassword = (EditText) findViewById(com.example.waleed.unitenapp123456.R.id.pword);

            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();

            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(email, password);
            return json;
        }
        //after login button pressed
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
               if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_CREATED_AT));
                       /**
                        *If JSON array details are stored in SQlite it launches the User Panel.
                        **/
                        Intent upanel = new Intent(getApplicationContext(), Main.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    }else{

                        pDialog.dismiss();
                        loginErrorMsg.setText("Incorrect username/password");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }
    /*
    public boolean isValidEmailAddress(String email) {
        String ePattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);

        return m.matches();
    }
    */
    /*
    public boolean isValidEmailAddress(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }*/

    boolean isValidEmailAddress(String Email) {
        String pttn = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern p = Pattern.compile(pttn);
        Matcher m = p.matcher(Email);

        if (m.matches()) {
            return true;
        }
        return false;
    }
}