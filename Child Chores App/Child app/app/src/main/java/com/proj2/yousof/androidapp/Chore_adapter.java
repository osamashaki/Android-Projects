package com.proj2.yousof.androidapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Chore_adapter extends ArrayAdapter<Chore> {



    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESC = "description";
    private static final String TAG_CHILDID = "child_id";
    private static final String TAG_CHDATE = "choredate";
    private static final String TAG_STATUS = "status";

    int groupid;
    ArrayList<Chore> records;
    Context context;

    ArrayList<HashMap<String, String>> choreslist;

    private Activity activity;
    private LayoutInflater inflater;

    public Chore_adapter(Context context, int vg, int id, ArrayList<Chore> records) {
        //activity = a;
        //this.feedbackslist = fList;
        super(context, vg, id, records);
        this.context = context;
        groupid = vg;
        this.records = records;
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(groupid, parent, false);

        // Declare Variables and assign values.

        TextView textchoreId = (TextView) itemView.findViewById(R.id.txtchoreId);
        TextView textchoretitle = (TextView) itemView.findViewById(R.id.txtchoreTitle);
        TextView textchoredate = (TextView) itemView.findViewById(R.id.txtchoreDate);
        TextView textchoreno = (TextView) itemView.findViewById(R.id.txtchoreNo);


        //if (textfeedtitle.length() > 25)
          //  textfeedtitle = textfeedtitle.substring(0, 24) + "...";

        textchoreId.setText(records.get(position).getchid());

        textchoretitle.setText(records.get(position).getchtitle());

        textchoredate.setText(records.get(position).getchd());

        textchoreno.setText(String.valueOf(position + 1));



        return itemView;


    }
    /*
    public void clearData() {

        feedbackslist.clear();
    }*/
}

