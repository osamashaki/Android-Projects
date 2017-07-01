package com.android.ukmproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ukmproject.model.Item;
import com.android.ukmproject.utils.Configuration;
import com.android.ukmproject.volley.AppController;
import com.android.ukmproject.volley.OnDownloadTaskCompleted;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class RestDetails extends FragmentActivity implements View.OnClickListener {

    String rid = "0", phonenumber;
    public String loc, resname;
    public double lat =0, longt=0;
    TextView rname, rmobile, ropenhours, rdishes, raddress,rlocation;
    Button call;

    // Google Map
    private GoogleMap googleMap;

    private static final String TAG_ITEMS_REQUEST = "Item_Details";
    private static final String URL = Configuration.SERVER_IP + "Get_Items_details.php";

    private static final String TAG_ITEM = "item";
    private static final String TAG_ID = "id";
    private static final String TAG_RESNAME = "resname";
    private static final String TAG_RESMOBILE = "resmobile";
    private static final String TAG_OPHOURS = "openinghours";
    private static final String TAG_DISHES = "dishes";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_PHOTO = "imagepath";


    private NetworkImageView itemDetails_thumbnail;

    private AppController app;
    private ImageLoader imageLoader;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_details);

        app = AppController.getInstance();
        imageLoader = AppController.getInstance().getImageLoader();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rid = extras.getString("rid");

            ConnectAndGet();
        } else {
            ShowToast("No restaurant details was found");
        }
        rname = (TextView) findViewById(R.id.txtrestname);
        rmobile = (TextView) findViewById(R.id.txtmobile);
        ropenhours = (TextView) findViewById(R.id.txtopenhours);
        rdishes = (TextView) findViewById(R.id.txtdishes);
        raddress = (TextView) findViewById(R.id.txtaddress);
        itemDetails_thumbnail = (NetworkImageView) findViewById(R.id.Details_thumbnail);
        rlocation = (TextView) findViewById(R.id.txtlocation);

        call = (Button) findViewById(R.id.btncall);
        call.setOnClickListener(this);






    }


    private void ConnectAndGet() {
        try {
            Map<String, String> paramsLists = new HashMap<String, String>();
            paramsLists.put("itemId", rid);

            app.MakeJsonObjectRequest(URL, TAG_ITEMS_REQUEST, "POST", paramsLists, false, new OnDownloadTaskCompleted() {
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
        }
    }

    public void ParseJSONObject(JSONObject response) {
        try {
            JSONArray responseArray = response.getJSONArray(TAG_ITEM);
            try {
                JSONObject obj = responseArray.getJSONObject(0);
                Item item = new Item();

                item.setId(obj.getString(TAG_ID));
                item.setName(obj.getString(TAG_RESNAME));
                item.setMobile(obj.getString(TAG_RESMOBILE));
                item.setHours((obj.getString(TAG_OPHOURS)));
                item.setDishes(obj.getString(TAG_DISHES));
                item.setAddress(obj.getString(TAG_ADDRESS));
                item.setLocation(obj.getString(TAG_LOCATION));
                item.setPhotopath(obj.getString(TAG_PHOTO));


                if (item.getPhotopath().toString().trim() != null && !item.getPhotopath().toString().trim().equalsIgnoreCase("")) {
                    itemDetails_thumbnail.setImageUrl(Configuration.SERVER_IP + "uploads/" + item.getPhotopath(), imageLoader);
                } else
                    itemDetails_thumbnail.setImageUrl(Configuration.SERVER_IP + "uploads/no_image.png", imageLoader);

                //show data on activity
                rname.setText(item.getName());
                rmobile.setText(item.getMobile());
                ropenhours.setText(item.getHours());
                rdishes.setText(item.getDishes());
                raddress.setText(item.getAddress());
                rlocation.setText(item.getLocation());

                resname = item.getName();
                phonenumber = item.getMobile();
                loc = item.getLocation();
                getlocation(loc);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getlocation(String loc)
    {
        String l = loc; //this.loc = loc;

        lat = Double.parseDouble(l.substring(0,l.indexOf(",")));
        longt = Double.parseDouble(l.substring(l.indexOf(", ")+1,l.length()));

        //Toast.makeText(RestDetails.this,"lat: " + lat , Toast.LENGTH_LONG).show();
        //Toast.makeText(RestDetails.this,"longt: " + longt , Toast.LENGTH_LONG).show();

        try {
            // Loading map
            InitilizeMap(lat,longt);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btncall:
                Log.e("Details Activity", "Call clicked");

                String number = phonenumber;
                //Toast.makeText(RestDetails.this, "n: " + number, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + number));
                startActivity(i);

        }

    }
    /**
     * function to load map. If map is not created it will create it for you
     * */
    public void InitilizeMap(double l, double lon) {

        double latitude = l; //3.146817;
        double longitude = lon; //101.710719;

        if (googleMap == null) {

            // latitude and longitude


            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(14).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(resname);
            // adding marker
            googleMap.addMarker(marker);
            //zoom
            googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
            googleMap.getUiSettings().setMapToolbarEnabled(true);
            //googleMap.getUiSettings().setCompassEnabled(true);
            //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        }
        // check if map is created successfully or not
        if (googleMap == null) {
            Toast.makeText(this.getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    protected void ShowToast(String msg) {
        if (msg != null && !msg.equals("")) {
            Toast.makeText(this.getApplicationContext(), msg.replaceAll("[\\[\\]\"]", ""), Toast.LENGTH_SHORT).show();
        }
    }

    public void onResume() {
        super.onResume();
        //InitilizeMap();
    }




}
