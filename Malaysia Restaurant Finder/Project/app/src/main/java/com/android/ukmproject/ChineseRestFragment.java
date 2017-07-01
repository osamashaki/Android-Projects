package com.android.ukmproject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ukmproject.model.Item;
import com.android.ukmproject.utils.Configuration;
import com.android.ukmproject.utils.ConnectionDetector;
import com.android.ukmproject.utils.CustomListAdapter;
import com.android.ukmproject.volley.AppController;
import com.android.ukmproject.volley.OnDownloadTaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChineseRestFragment extends Fragment {

    private static final String TAG_ITEMS_REQUEST = "Items";
    private static final String URL = Configuration.SERVER_IP + "Get_Items_ch.php";

    private static final String TAG_ITEMS = "items"; //tags parameters names from php page
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "resname";
    private static final String TAG_MOBILE = "resmobile";
    private static final String TAG_PHOTO = "imagepath";

    private List<Item> itemList = new ArrayList<Item>();

    private ListView listView;

    private CustomListAdapter adapter;
    private AppController app;
    protected View mView;

    private String msg = "";
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;


    public ChineseRestFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_chinese_rest, container, false);
        this.mView = rootView;

        app = AppController.getInstance();
        // creating connection detector class instance
        cd = new ConnectionDetector(this.getActivity().getApplicationContext());

        listView = (ListView) mView.findViewById(R.id.list);

        adapter = new CustomListAdapter(getActivity(), itemList);
        listView.setAdapter(adapter);

        Runnable mRunnable;
        Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                ConnectAndGet();
            }
        };
        mHandler.postDelayed(mRunnable, Configuration.DELAYMILLIS);


        return rootView;
    }

    private void ConnectAndGet() {
        //check internet connection
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            try {
                app.MakeJsonObjectRequest(URL, TAG_ITEMS_REQUEST, "GET", null, false, new OnDownloadTaskCompleted() {
                    @Override
                    public void onTaskCompleted(JSONObject response, boolean error, String message) {
                        if (!error) {
                            ParseJSONObject(response);
                        } else {
                            ShowToast(message);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }//end of try
            //showAlertDialog(this.getActivity(), "Internet Connection","You have internet connection", true);
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(this.getActivity(), "No Internet Connection",
                    "You don't have internet connection.", false);
        }
    }

    private void ParseJSONObject(JSONObject response) {
        try {
            JSONArray responseArray = response.getJSONArray(TAG_ITEMS);
            for (int i = 0; i < responseArray.length(); i++) {
                try {
                    JSONObject obj = responseArray.getJSONObject(i);
                    Item item = new Item();
                    item.setId(obj.getString(TAG_ID));
                    item.setName(obj.getString(TAG_NAME));
                    item.setMobile(obj.getString(TAG_MOBILE));
                    item.setPhotopath(obj.getString(TAG_PHOTO));

                    itemList.add(item);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    String itemId = ((TextView) view.findViewById(R.id.id)).getText().toString();
                    Intent i = new Intent(getActivity(),RestDetails.class);
                    i.putExtra("rid", itemId);
                    startActivity(i);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    protected void ShowToast(String msg) {
        if (msg != null && !msg.equals("")) {
            Toast.makeText(getActivity(), msg.replaceAll("[\\[\\]\"]", ""), Toast.LENGTH_SHORT).show();
        }
    }

}
