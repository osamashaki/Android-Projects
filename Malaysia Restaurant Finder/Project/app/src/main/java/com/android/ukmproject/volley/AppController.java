package com.android.ukmproject.volley;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ERROR = "error";
    private static final String TAG_ERROR_MSG = "error_msg";


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private LruBitmapCache mLruBitmapCache;

    private static AppController mInstance;

    private StringRequest jsonObjReq = null;
    private JsonArrayRequest jsonaArrayReq = null;

    private String success = "", errorMsg = "";
    private int requestMethod;
    private JSONObject responseJsonObject;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            //			DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 100 * 1024 * 1024);
            //			mRequestQueue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
            //
            //			mRequestQueue.start();
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        Log.i("+++Adding Request+++", tag + " || " +req.getUrl());
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
            Log.i("+++ Cancelled +++", tag.toString());
        }
    }

    public void SkipStrict() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void MakeJsonObjectRequest(String url, String tag, String method, final Map<String, String> paramsLists,
                                      boolean isCached, final OnDownloadTaskCompleted taskCompleted) throws JSONException {
        errorMsg = "";
        SkipStrict();

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(url);
        if (entry != null && isCached) {
            try {
                String data = new String(entry.data, "UTF-8");
                Log.i("+++ Cache +++", data);

                taskCompleted.onTaskCompleted(new JSONObject(data), false, errorMsg);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (method.equalsIgnoreCase("POST"))
                this.requestMethod = Method.POST;
            else if (method.equalsIgnoreCase("GET"))
                this.requestMethod = Method.GET;

            jsonObjReq = new StringRequest(requestMethod, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        Log.i("+++ response +++", response.toString());

                        try {
                            responseJsonObject = new JSONObject(response);

                            success = responseJsonObject.getString(TAG_SUCCESS);
                            if (success.equals("1")) {
                                taskCompleted.onTaskCompleted(responseJsonObject, false, null);
                            }
                            else {
                                errorMsg = responseJsonObject.getString(TAG_ERROR_MSG);
                                taskCompleted.onTaskCompleted(responseJsonObject, true, errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorMsg = "Error: " + error.getMessage();
                    Log.i("+++ errorMsg +++", errorMsg);

                    taskCompleted.onTaskCompleted(null, true, errorMsg);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = paramsLists;
                    return params;
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(7000, 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
            jsonObjReq.setShouldCache(true);

            AppController.getInstance().addToRequestQueue(jsonObjReq, tag);
        }
    }

}