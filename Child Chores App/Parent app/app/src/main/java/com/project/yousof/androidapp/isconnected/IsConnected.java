package com.project.yousof.androidapp.isconnected;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

public class IsConnected extends Activity {

    @SuppressWarnings("unused")
	private Context _context;
    
    public IsConnected(Context context){
        this._context = context;
    }
 
    public boolean isConnectedToServer(String urlToCheck) {
//    	  try {
//	        	URL url = new URL(urlToCheck);
//	        	executeReq(url);
//	        	Toast.makeText(getApplicationContext(), "Available", Toast.LENGTH_LONG).show();
//	        }
//	        catch(Exception e) {
//	        	Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
//	        }
    	  
    	    HttpGet httpGet = new HttpGet(urlToCheck);
    	    HttpParams httpParameters = new BasicHttpParams();
    	    // Set the timeout in milliseconds until a connection is established.
    	    // The default value is zero, that means the timeout is not used.
    	    int timeoutConnection = 1000;
    	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	    // Set the default socket timeout (SO_TIMEOUT)
    	    // in milliseconds which is the timeout for waiting for data.
    	    int timeoutSocket = 3000;
    	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

    	    DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
    	    try{
    	        Log.d("Access", "Checking network connection...");
    	        httpClient.execute(httpGet);
    	        Log.d("Access", "Connection OK");
    	        return true;
    	    }
    	    catch(ClientProtocolException e){
    	        e.printStackTrace();
    	    }
    	    catch(IOException e){
    	        e.printStackTrace();
    	    }

    	    Log.d("No Access", "Connection unavailable");
			return false;
    	
    }
    
    public void SkipStrict(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
    }
    
//    private void executeReq(URL url) throws IOException {
//		// TODO Auto-generated method stub
//		
//		HttpURLConnection con = (HttpURLConnection) url.openConnection();
//		con.setReadTimeout(3000);
//		con.setConnectTimeout(3500);
//		con.setRequestMethod("GET");
//		con.setDoInput(true);
//		
//		// Connect
//		con.connect();
//	} 
}
