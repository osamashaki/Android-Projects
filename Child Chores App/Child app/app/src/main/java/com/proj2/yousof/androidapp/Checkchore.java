package com.proj2.yousof.androidapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proj2.yousof.androidapp.configuration.Configuration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Checkchore extends Activity {

    private static String url = "stugetallchores.php";

    Activity context;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    ProgressDialog pd;
    Chore_adapter adapter;
    ListView listFeedback;
    ArrayList<Chore> records;

    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CHDATE = "choredate";
    String  sid;

    String chid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkchore);

        context=this;
        records = new ArrayList<>();
        listFeedback=(ListView)findViewById(R.id.list);
        adapter=new Chore_adapter(context, R.layout.chore_listview,R.id.txtchoreTitle, records);
        listFeedback.setAdapter(adapter);

        sid = getIntent().getStringExtra("sid");
        //Toast.makeText(getApplicationContext(), "sid: "+ sid, Toast.LENGTH_LONG).show();


    }


    public void onStart(){
        super.onStart();
        //execute background task
        BackTask bt=new BackTask();
        bt.execute();

    }

    //background process to make a request to server and list product information
    private class BackTask extends AsyncTask<Void,Void,Void> {
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setTitle("Retrieving chore data");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setIndeterminate(true);
            pd.show();

        }

        protected Void doInBackground(Void...params){

            InputStream is=null;
            String result="";
            try{

                List<NameValuePair> params2;
                params2 = new ArrayList<>();
                params2.add(new BasicNameValuePair("cid", sid));

                httpclient=new DefaultHttpClient();
                httppost= new HttpPost(Configuration.SERVER_IP + url);
                httppost.setEntity(new UrlEncodedFormEntity(params2));
                response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            }catch(Exception e){

                if(pd!=null)
                    pd.dismiss(); //close the dialog if error occurs
                Log.e("ERROR", e.getMessage());

            }

            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                is.close();
                result=sb.toString();
            }catch(Exception e){
                Log.e("ERROR", "Error converting result "+e.toString());

            }

            try{
                records = new ArrayList<Chore>();

                result=result.substring(result.indexOf("["));
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data =jArray.getJSONObject(i);
                    Chore p = new Chore();
                    p.setchid(json_data.getString(TAG_ID));
                    p.setchtitle(json_data.getString(TAG_TITLE));
                    p.setchd(json_data.getString(TAG_CHDATE));

                    records.add(p);
                }

            }
            catch(Exception e){
                Log.e("ERROR", "Error pasting data " + e.toString());

            }

            return null;
        }

        protected void onPostExecute(Void result){

            if(pd!=null) pd.dismiss(); //close dialog
            Log.e("size", records.size() + "");

            adapter.notifyDataSetChanged(); //notify the ListView to get new records

            if (records.size() != 0){
                adapter = new Chore_adapter(context, R.layout.chore_listview, R.id.txtchoreTitle, records);
                if(adapter.getCount() != 0 ){
                    listFeedback.setAdapter(adapter);
                    listFeedback.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
                            chid = ((TextView) view.findViewById(R.id.txtchoreId)).getText().toString();
                            Intent next = new Intent(getApplicationContext(), Chore_details.class);
                            next.putExtra("chid", chid);
                            next.putExtra("sid", sid);

                            startActivity(next);
                        }

                    });
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No chores were found", Toast.LENGTH_LONG).show();
            }

        }

    }
}
