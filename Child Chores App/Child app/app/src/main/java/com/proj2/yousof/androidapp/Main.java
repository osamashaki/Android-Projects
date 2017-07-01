package com.proj2.yousof.androidapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.proj2.yousof.androidapp.library.DatabaseHandler;
import com.proj2.yousof.androidapp.library.UserFunctions;

import org.json.JSONObject;

import java.util.HashMap;



public class Main extends Activity {

    Button chkchores;
    Button chkpoints;
    Button chkallowance;
    Button btnLogout;
    Button changepas;


    String sid;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        chkchores = (Button) findViewById(R.id.checkchores);
        chkpoints = (Button) findViewById(R.id.checkpoints);
        chkallowance = (Button) findViewById(R.id.checkallowance);
        changepas = (Button) findViewById(R.id.btchangepass);
        btnLogout = (Button) findViewById(R.id.logout);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();

        JSONObject json = null;
        String name = null;
        //final TextView login = (TextView) findViewById(R.id.textwelcome);

        sid = getIntent().getStringExtra("sid");
        //Toast.makeText(getApplicationContext(), "sid: "+ sid, Toast.LENGTH_LONG).show();



        chkchores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent send = new Intent(getApplicationContext(), Checkchore.class);
                send.putExtra("sid",sid);
                startActivity(send);
            }

        });

        chkpoints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent check = new Intent(getApplicationContext(), Points.class);
                check.putExtra("sid",sid);
                startActivity(check);
            }

        });
        //
        chkallowance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent check = new Intent(getApplicationContext(), Allowance.class);
                check.putExtra("sid",sid);
                startActivity(check);
            }

        });



        changepas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);
                chgpass.putExtra("sid",sid);
                startActivity(chgpass);
            }

        });

       /**
        *Logout from the User Panel which clears the data in Sqlite database
        **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });
/**
 * Sets user first name and last name in text view.
 **/

        //login.setText("Welcome to Uniten Chore App ");// + name);



    }





}