package com.example.kucing_adik;

import android.support.v7.app.ActionBarActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;
 
public class MainActivity extends ActionBarActivity {
	
	TextView tv1;
	Button btn1;
	MediaPlayer mp;
    ImageButton imgbtn1;
    MediaPlayer player;
    ImageView img1;
	MediaPlayer mp2;
    private boolean mIsBound = false;
    private MusicService mServ;
    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//creating object Service
		mServ = new MusicService();
		//binding service
		doBindService();
		//create intent for connect this
		Intent music = new Intent();
		music.setClass(this,MusicService.class);
		startService(music);
		//inflate the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	    setContentView(R.layout.activity_main);
		
		tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setTextColor(Color.RED);
		mp = MediaPlayer.create(getBaseContext(), R.raw.sound2);
		mp2 = MediaPlayer.create(getBaseContext(), R.raw.first);
		tv1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// TODO Auto-generated method stub
				mp.start();
				
				Intent i = new Intent(getApplicationContext(), Two.class);
				startActivity(i);
				finish();
				//mp.release();
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
	protected void onDestroy() {
        super.onDestroy();
        mp.release();
        //mServ.stopMusic();
        //stopService(new Intent(this, MusicService.class));
       
    }
	protected void onPause() {
        super.onPause();
        mp.release();
        //mServ.stopMusic();
        //stopService(new Intent(this, MusicService.class));
       
    }
	 public void onStop() { 
         super.onStop(); 
         //stopService(new Intent(this, MusicService.class));
 } 
	
	//
	
	//
	private ServiceConnection Scon = new ServiceConnection(){

		public  void onServiceConnected ( ComponentName name ,  IBinder 
			     binder )  {

			mServ = ((MusicService.ServiceBinder)binder).getService();


			    }

		public void onServiceDisconnected(ComponentName name) {
		    mServ = null;
		}
		};

		void doBindService(){
		    bindService(new Intent(this,MusicService.class),
		            Scon,Context.BIND_AUTO_CREATE);
		    mIsBound = true;
		}

		void doUnbindService()
		{
		    if(mIsBound)
		    {
		        unbindService(Scon);
		        mIsBound = false;
		    }
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
	

