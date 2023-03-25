package com.gacha.test;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CallAPI {

    /**
     * get link to api
     *
     * @param link route
     * @return a link to api
     */
    public static String getLink(String link) {
        String IP = "192.168.1.15";
        return "http://" + IP + ":3000/" + link;
    }

    public static void useVolley(Context context,
                                 String url,
                                 int method,
                                 @Nullable JSONArray jsonArray,
                                 Response.Listener<JSONArray> listener,
                                 @Nullable Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest stringRequest = new JsonArrayRequest(method, url, jsonArray, listener, errorListener);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void useVolleyJSONObject(Context context,
                                 String url,
                                 int method,
                                 @Nullable JSONObject jsonObject,
                                 Response.Listener<JSONObject> listener,
                                 @Nullable Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest stringRequest = new JsonObjectRequest(method, url, jsonObject, listener, errorListener);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
