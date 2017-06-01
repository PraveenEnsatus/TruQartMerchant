package com.ensatus.truqartmerchant.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.module.User;
import com.ensatus.truqartmerchant.service.LandingScreenService;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final String  TAG = MainActivity.class.getSimpleName();
    private TextView mSumInProcess, mSumDispatched, mSumDelivered, mTProcess, mTDispatched,
            mTDelivered, mTTotal;
    private DonutProgress donutProgress;
    private String mSumProcess,mSumDel,mSumDis,mTodayDelivered,mTodayDispatched,mTodayProcess,mTodayTotal;

    User user = MyApplication.getInstance().getPrefManager().getUser();
    private String MID = user.getmId();
    int mProgress;
    private TextView mProfName;
    private TextView mShopName;
    private CircleImageView mProfPic;
    private ImageLoader imageLoader;
    private Intent intent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSumInProcess = (TextView) findViewById(R.id.in_process);
        mSumDelivered = (TextView) findViewById(R.id.delivered);
        mSumDispatched = (TextView) findViewById(R.id.dispatched);
        mTDelivered = (TextView) findViewById(R.id.today_delivered);
        mTDispatched = (TextView) findViewById(R.id.today_pending);
        mTProcess  = (TextView) findViewById(R.id.today_in_process);
        mTTotal =(TextView) findViewById(R.id.total_orders);
        donutProgress =(DonutProgress) findViewById(R.id.donut_progress);
        mProfName = (TextView) findViewById(R.id.prof_name);
        mProfName.setText(user.getmName());
        mShopName = (TextView) findViewById(R.id.shop_name);
        mShopName.setText(user.getmShop());
        mProfPic = (CircleImageView) findViewById(R.id.prof_pic);
        imageLoader = MyApplication.getInstance().getImageLoader();
        imageLoader.get(user.getmIcon(), ImageLoader.getImageListener(mProfPic, R.drawable.a_s_tcustomericon, R.drawable.acoount));
        //donutProgress.setFinishedStrokeColor(R.color.lig_green);
       /* int progress = 10;
        donutProgress.setProgress(progress);
*/






        intent = new Intent(this, LandingScreenService.class);



        try {
            OIStatus();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



    public void Customers(View view){
        startActivity(new Intent(this, MyCustomersActivity.class));
    }
    public void Profile(View view){
        startActivity(new Intent(this, ProfileActivity.class));
    }
    public void Blank(View view){
        startActivity(new Intent(this, TransactionsActivity.class));
    }
    public void Broadcast(View view){
        startActivity(new Intent(this, BroadcastActivity.class));
    }
    public void Product(View view){
        startActivity(new Intent(this, ProductsActivity.class));
    }
    public void Events(View view){
        startActivity(new Intent(this, EventsActivity.class));
    }
    public void Reports(View view){
        startActivity(new Intent(this, ReportsActivity.class));
    }



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(LandingScreenService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "service stopped");
        stopService(intent);
        unregisterReceiver(broadcastReceiver);

    }


    private void updateUI(Intent intent) {
        mSumProcess = intent.getStringExtra("mSumProcess");
        mSumDel = intent.getStringExtra("mSumDelivered");
        mSumDis = intent.getStringExtra("mSumDispatched");
        mTodayDelivered = intent.getStringExtra("mtodDelivered");
        mTodayDispatched = intent.getStringExtra("mtodDispatched");
        mTodayProcess = intent.getStringExtra("mtodProcess");
        mTodayTotal = intent.getStringExtra("mTodTotal");
        mProgress = intent.getIntExtra("progress",0);
       /* Log.d(TAG, counter);
        Log.d(TAG, time);*/

        mSumInProcess.setText(mSumProcess);
        mSumDelivered.setText(mSumDel);
        mSumDispatched.setText(mSumDis);
        mTDelivered.setText(mTodayDelivered);
        mTDispatched.setText(mTodayDispatched);
        mTProcess.setText(mTodayProcess);
        mTTotal.setText(mTodayTotal);
        Float value = (Float.parseFloat(mTodayDelivered)/Float.parseFloat(mTodayTotal))*100;
        mProgress = Math.round(value);
        Log.e(TAG, "progress :" +Integer.valueOf(mTodayTotal)+"  "+Integer.valueOf(mTodayDelivered)+" " +mProgress);
        donutProgress.setProgress(mProgress);

    }

    public void OIStatus() throws JSONException{
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.MAIN_SUMMARY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONObject sumObj = obj.getJSONObject("summary");
                                mSumProcess = sumObj.getString("inprocess");
                                mSumDis = sumObj.getString("dispatched");
                                mSumDel = sumObj.getString("delivered");
                                JSONObject todayObj = obj.getJSONObject("today");
                                mTodayTotal = todayObj.getString("total");
                                mTodayProcess = todayObj.getString("inprocess");
                                mTodayDispatched = todayObj.getString("dispatched");
                                mTodayDelivered = todayObj.getString("delivered");
                                mSumInProcess.setText(mSumProcess);
                                mSumDelivered.setText(mSumDel);
                                mSumDispatched.setText(mSumDis);
                                mTDelivered.setText(mTodayDelivered);
                                mTDispatched.setText(mTodayDispatched);
                                mTProcess.setText(mTodayProcess);
                                mTTotal.setText(mTodayTotal);
                                Float value = (Float.parseFloat(mTodayDelivered) / Float.parseFloat(mTodayTotal)) * 100;
                                mProgress = Math.round(value);
                                Log.e(TAG, "progress :" + Integer.valueOf(mTodayTotal) + "  " + Integer.valueOf(mTodayDelivered) + " " + mProgress);
                                donutProgress.setProgress(mProgress);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e("TAG", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    if (error.getMessage() == null) {
                        // Toast.makeText(getApplicationContext(),  "Check Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(EndPoints.MERCHANT_ID, MID);


                    Log.e("TAG", "params: " + params.toString());
                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }

            };
            Log.e("TAG", "StrReq:" + strReq);
            MyApplication.getInstance().getRequestQueue().getCache().clear();
            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
            Log.e(TAG, "after");
        }
    }

}
