package com.example.kucing_adik;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.media.MediaPlayer;


public class Eleven extends ActionBarActivity {
	
	ImageButton btn1;
	ImageButton btn2;
	ImageButton btn3;
	MediaPlayer mp;
	ImageView img1;
	MediaPlayer mp2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.el11);
		
		//prev button
		btn1 = (ImageButton) findViewById(R.id.ImageButton1);
		mp = MediaPlayer.create(getBaseContext(), R.raw.sound2);
		mp2 = MediaPlayer.create(getBaseContext(), R.raw.eleven);
		btn1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// TODO Auto-generated method stub
				mp.start();
				Intent i = new Intent(getApplicationContext(), Ten.class);
				startActivity(i);
				finish();
			}
		});
		//next button
		btn2 = (ImageButton) findViewById(R.id.ImageButton2);
		btn2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// TODO Auto-generated method stub
				mp.start();
				Intent i = new Intent(getApplicationContext(), Twelve.class);
				startActivity(i);
				finish();
			}
		});
		//home button
		btn3 = (ImageButton) findViewById(R.id.ImageButton3);
		mp = MediaPlayer.create(getBaseContext(), R.raw.sound2);
		btn3.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// TODO Auto-generated method stub
				mp.start();
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				finish();
			}
		});
		//image sound click
		img1 = (ImageView) findViewById(R.id.imageView1);
		img1.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
					// TODO Auto-generated method stub
					mp2.start();
					
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
	            && (keyCode == KeyEvent.KEYCODE_BACK )
	            && event.getRepeatCount() == 0) {
	        // Take care of calling this method on earlier versions of
	        // the platform where it doesn't exist.
	    	
	        onBackPressed();
	    }

	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
	    // This will be called either automatically for you on 2.0
	    // or later, or by the code above on earlier versions of the
	    // platform.
		finish();
		stopService(new Intent(this, MusicService.class));
		
	    return;
	}
}
