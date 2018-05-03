package com.rescribe.doctor.dms.network;
/**
 * @author Sandeep Bahalkar
 *
 */

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RequestPool {
    private RequestQueue mRequestQueue;
    private Context mContext;

    private RequestPool(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestPool getInstance(Context context) {

        return new RequestPool(context);
    }

    public void cancellAllPreviousRequestWithSameTag(String tag){
    	mRequestQueue.cancelAll(tag);
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());

        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(JsonObjectRequest req) {
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(StringRequest req) {
        getRequestQueue().add(req);
    }

}

