package com.android.ukmproject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SuggestRestFragment extends Fragment {

    protected View mView;



    public SuggestRestFragment(){}
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_suggest_rest, container, false);
        this.mView = rootView;


        return rootView;
    }


}
