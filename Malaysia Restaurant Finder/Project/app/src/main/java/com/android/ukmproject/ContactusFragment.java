package com.android.ukmproject;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ukmproject.utils.Configuration;
import com.android.ukmproject.volley.AppController;
import com.android.ukmproject.volley.OnDownloadTaskCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ContactusFragment extends Fragment {

    private static final String TAG_ITEM_ADD_REQUEST = "Item_Add";
    private static final String URL = Configuration.SERVER_IP + "sendmsg.php";

    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_MSG = "msg";
    private AppController app;


    EditText name,email, phone, msg;
    Button send;
    protected View mView;


    public ContactusFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_contactus, container, false);
        this.mView = rootView;

        app = AppController.getInstance();

        name = (EditText) mView.findViewById(R.id.sendername);
        email = (EditText) mView.findViewById(R.id.senderemail);
        phone = (EditText) mView.findViewById(R.id.senderphone);
        msg = (EditText) mView.findViewById(R.id.sendermsg);




        send = (Button) mView.findViewById(R.id.btnsend);
        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ConnectAndSend();
                //Toast.makeText(getActivity(),"Message sent" , Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    //
    private void ConnectAndSend() {
        boolean nameChk = CheckForEmptyText(name);
        boolean emailChk = CheckForEmptyText(email);
        boolean msgChk = CheckForEmptyText(msg);


        if (nameChk == false && emailChk == false && msgChk == false) {
            try {
                Map<String, String> paramsLists = new HashMap<String, String>();
                paramsLists.put(TAG_NAME, name.getText().toString());
                paramsLists.put(TAG_EMAIL, email.getText().toString());
                paramsLists.put(TAG_PHONE, phone.getText().toString());
                paramsLists.put(TAG_MSG, msg.getText().toString());


                app.MakeJsonObjectRequest(URL, TAG_ITEM_ADD_REQUEST, "POST", paramsLists, false, new OnDownloadTaskCompleted() {
                    @Override
                    public void onTaskCompleted(JSONObject response, boolean error, String message) {
                        if (!error) {

                            Toast.makeText(getActivity(),"Message sent successfully" , Toast.LENGTH_LONG).show();
                            name.setText("");
                            email.setText("");
                            phone.setText("");
                            msg.setText("");



                        } else {

                            ShowToast(message);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }//



    //
    public Boolean CheckForEmptyText(final TextView v) {
        if (v.getText().length() == 0 || v.getText().toString().startsWith(" ")) {
            if (v.getId() == name.getId()) {
                setErrorMsg("Please provide a name", name, 0);
            } else if (v.getId() == email.getId()) {
                setErrorMsg("Please provide the email", email, 0);
            } else if (v.getId() == msg.getId()) {
                setErrorMsg("Please provide the message", msg, 0);
            }

            return true;
        } else {
            if (v.getId() == name.getId()) {
                setErrorMsg(" ", name, 1);
            } else if (v.getId() == email.getId()) {
                setErrorMsg(" ", email, 1);
            } else if (v.getId() == msg.getId()) {
                setErrorMsg(" ", msg, 1);
            }

            return false;
        }
    }

    public static void setErrorMsg(String msg, EditText viewId, int status) {
        int ecolor = Color.RED;
        String estring = msg;
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = null;
        if (status == 0) {
            ssbuilder = new SpannableStringBuilder(estring);
            ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
            viewId.setError(ssbuilder);
        } else
            viewId.setError(ssbuilder);
    }

    protected void ShowToast(String msg) {
        if (msg != null && !msg.equals("")) {
            Toast.makeText(getActivity(), msg.replaceAll("[\\[\\]\"]", ""), Toast.LENGTH_SHORT).show();
        }
    }




}
