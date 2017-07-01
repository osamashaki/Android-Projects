package com.android.ukmproject.volley;

import org.json.JSONObject;


public interface OnDownloadTaskCompleted {
    public void onTaskCompleted(JSONObject response, boolean error, String message);
}
