package com.ensatus.truqartmerchant.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Praveen on 25-11-2016.
 */

public class LandingScreenService extends Service {
    private static final String TAG = LandingScreenService.class.getSimpleName();
    public static final String BROADCAST_ACTION = "com.ensatus.truqartmerchant.service.landing";
    private String MID = "745d03d7-6e32-4db7-a991-5e5cf664e8e6"; // hard coded for now
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        Log.e("in landing oncreate","");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 60000); // 60 seconds
        }
    };
    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");
        OIStatus();

       /* intent.putExtra("time", new Date().toLocaleString());
        intent.putExtra("counter", String.valueOf(++counter));*/

    }


    public void OIStatus() {

        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.MAIN_SUMMARY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " +response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject sumObj = obj.getJSONObject("summary");
                            String mSumProcess = sumObj.getString("inprocess");
                            String mSumDis = sumObj.getString("dispatched");
                            String mSumDel = sumObj.getString("delivered");
                            JSONObject todayObj = obj.getJSONObject("today");
                            String mTodayProcess = todayObj.getString("inprocess");
                            String mTodayDispatched = todayObj.getString("dispatched");
                            String mTodayDelivered = todayObj.getString("delivered");
                            String mTodayTotal = todayObj.getString("total");
                            int mProgress = Integer.valueOf(mTodayDelivered)/Integer.valueOf(mTodayTotal);
                             intent.putExtra("mSumProcess",mSumProcess );
                             intent.putExtra("mSumDelivered", mSumDel);
                            intent.putExtra("mSumDispatched", mSumDis);
                            intent.putExtra("mtodProcess",mTodayProcess );
                            intent.putExtra("mtodDelivered", mTodayDelivered);
                            intent.putExtra("mtodDispatched", mTodayDispatched);
                            intent.putExtra("mTodTotal",mTodayTotal );
                            intent.putExtra("progress", mProgress);
                            sendBroadcast(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                },new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse (VolleyError error){
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("TAG", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                if (error.getMessage() == null) {
                  //  Toast.makeText(getApplicationContext(), "Check ur internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }){


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(EndPoints.MERCHANT_ID, MID);




                Log.e("TAG", "params: " + params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };
        Log.e("TAG", "StrReq:" + strReq);
        MyApplication.getInstance().getRequestQueue().getCache().clear();
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
        Log.e(TAG, "after");

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
