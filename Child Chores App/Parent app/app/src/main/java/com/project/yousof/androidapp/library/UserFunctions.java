package com.project.yousof.androidapp.library;

import android.content.Context;

import com.project.yousof.androidapp.configuration.Configuration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
    //private static String APIURL = "http://10.0.2.2/complaintsapp/adminlogin/";
    private static String APIURL = Configuration.SERVER_IP + "adminlogin/";
    private static String ADDURL = Configuration.SERVER_IP + "adminlogin/addchore.php";


    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String assign_tag = "assign";




    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String userid){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("userid", userid));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }

    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(APIURL, params);
        return json;
    }


    /**
     * Function to  Register // no need to change
     **/
    public JSONObject registerUser(String fname, String lname, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        JSONObject json = jsonParser.getJSONFromUrl(APIURL,params);
        return json;
    }
    //add chore
    public JSONObject addchore(String t, String d, String n){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", assign_tag));
        params.add(new BasicNameValuePair("title", t));
        params.add(new BasicNameValuePair("desc", d));
        params.add(new BasicNameValuePair("name", n));


        JSONObject json = jsonParser.getJSONFromUrl(ADDURL, params);
        return json;
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}

