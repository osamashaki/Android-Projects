package com.example.waleed.unitenapp123456;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Ques1 extends Activity implements View.OnClickListener  {

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.option1:
                //Toast.makeText(getApplicationContext(), "Option 1", Toast.LENGTH_LONG).show();
                points_a =0;
                points_a = points_a + 1;
                points_b=0;
                points_c=0;
                Toast.makeText(getApplicationContext(), "Answers: "+ points_a, Toast.LENGTH_LONG).show();

                break;
            case R.id.option2:
                //Toast.makeText(getApplicationContext(), "Option 2", Toast.LENGTH_LONG).show();
                points_b =0;
                points_b = points_b + 1;
                points_a=0;
                points_c=0;
                Toast.makeText(getApplicationContext(), "Answers: "+ points_b, Toast.LENGTH_LONG).show();

                break;
            case R.id.option3:
                //Toast.makeText(getApplicationContext(), "Option 3", Toast.LENGTH_LONG).show();
                points_c =0;
                points_c = points_c + 1;
                points_a=0;
                points_b=0;
                Toast.makeText(getApplicationContext(), "Answers: "+ points_c, Toast.LENGTH_LONG).show();

                break;
        }


    }

    Button btn1;
    Button btn2;
    RadioButton a;
    RadioButton b;
    RadioButton c;
    Integer points_a=0,points_b=0,points_c=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ques1);

        btn1 = (Button) findViewById(R.id.next);

        a = (RadioButton) findViewById(R.id.option1);
        b = (RadioButton) findViewById(R.id.option2);
        c = (RadioButton) findViewById(R.id.option3);

        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);


        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(!a.isChecked() && !b.isChecked() && !c.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "Please choose your answer!", Toast.LENGTH_LONG).show();
                }
                else
                {

                    Intent myIntent = new Intent(view.getContext(), Ques2.class);
                    myIntent.putExtra("ap", points_a);
                    myIntent.putExtra("bp", points_b);
                    myIntent.putExtra("cp", points_c);

                    startActivity(myIntent);
                    finish();
                }
            }
        });




    }

}
