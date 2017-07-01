package com.example.waleed.unitenapp123456;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.waleed.unitenapp123456.library.DatabaseHandler;

import java.util.HashMap;



public class Main extends Activity {

    Button btnstart;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        btnstart = (Button) findViewById(R.id.start);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();


        /**
        *Logout from the User Panel which clears the data in Sqlite database
        **/
        btnstart.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent i = new Intent(getApplicationContext(), Ques1.class);
                startActivity(i);
                finish();
            }
        });
/**
 * Sets user first name and last name in text view.
 **/
        final TextView login = (TextView) findViewById(R.id.textwelcome);
        //login.setText("Welcome  "+user.get("fname"));
        //final TextView lname = (TextView) findViewById(R.id.lname);
        //lname.setText(user.get("lname"));


    }}