package com.ensatus.truqartmerchant.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.adapter.MyOrdersAdapter;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.module.OrdersG;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionsActivity extends AppCompatActivity {
    private static final String TAG = TransactionsActivity.class.getSimpleName();
    protected boolean mCardRequested;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<OrdersG> myDataset = new ArrayList<>();
    User user = MyApplication.getInstance().getPrefManager().getUser();
    private String MID = user.getmId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        mCardRequested = false;
        mRecyclerView = (RecyclerView) findViewById(R.id.orders_rv);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use Linear Layout Manager for grid view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify  Adapter
        mAdapter = new MyOrdersAdapter(myDataset,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        try {

            getMyOrders();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();


        mRecyclerView.addOnItemTouchListener(new MyApplication.RecyclerTouchListener(getApplicationContext(), mRecyclerView, new MyApplication.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                OrdersG order = myDataset.get(position);
                Toast.makeText(getApplicationContext(), order.getOid() + " is selected!", Toast.LENGTH_SHORT).show();

                Log.e("name", "" + order.getProdArray());
                Intent intent = new Intent(TransactionsActivity.this, OrderDetailsActivity.class);
                intent.putExtra("oid", order.getOid());
                intent.putExtra("cname", order.getCname());
                intent.putExtra("mshopname", order.getMshopname());
                intent.putExtra("parray", String.valueOf(order.getProdArray()));
                intent.putExtra("total",order.getTotal());
                intent.putExtra("orderts", order.getOrderts());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /*public void getMyOrders() throws JSONException {
        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.MYORDERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e(TAG, "response: "+response.toString());

                            JSONObject obj = new JSONObject(response);
                            System.out.print(obj);
                            //String status = obj.getString(EndPoints.STATUS);
                            //String message = obj.getString(EndPoints.MESSAGE);
                            //if (message.equals("SUCCESS")&& status.equals("100")){
                                JSONArray product = obj.getJSONArray(EndPoints.PURCHASE_LIST);
                                for (int i=0; i< product.length(); i++) {
                                    JSONObject c = product.getJSONObject(i);
                                    //JSONObject p = c.getJSONObject(EndPoints.PRODUCT);
                                    Order order = new Order(c.getString(EndPoints.P_NAME),
                                            c.getString(EndPoints.PROD_PRICE),
                                            "2",
                                            c.getString(EndPoints.P_DEL_DATE),
                                            c.getString(EndPoints.P_ORD_DATE),
                                            c.getString(EndPoints.P_PAY_DATE),
                                            c.getString(EndPoints.P_STATUS), c.getString(EndPoints.P_IMAGE1)
                                           );
                                    myDataset.add(i, order);
                                    Log.e(TAG, "product: " + order.getpName() + order.getpImg());
                                   *//* mProName = prod.getmProName();
                                    mProPrice = prod.getmPrice();*//*
                                }
                           *//* }else if (status.equals("-1")){
                                Toast.makeText(getApplicationContext(),"Products Not Found", Toast.LENGTH_SHORT).show();
                            }*//*
                            mAdapter.notifyDataSetChanged();
                            mCardRequested = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse (VolleyError error){
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){



            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(EndPoints.CATEGORY_ID, CID);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                // params.put("Content-Type","application/json");
                return params;
            }

        };
        Log.e(TAG, "StrReq:" +strReq);
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }*/

    public void getMyOrders() throws JSONException {
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.ORDERS_BY_MER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response.toString());
                            try {


                                JSONObject obj = new JSONObject(response);
                                System.out.print(obj);
                                String status = obj.getString(EndPoints.STATUS);
                                String message = obj.getString(EndPoints.MESSAGE);
                                if (status.equals("100")) {
                                    JSONArray orderArray = obj.getJSONArray(EndPoints.ORDER_INFO);
                                    //JSONArray orderArray = obj.getJSONArray("product_info");
                                    for (int i = 0; i < orderArray.length(); i++) {
                                        JSONObject ordObj = orderArray.getJSONObject(i);

                                        JSONArray prodArray = ordObj.getJSONArray("proddetails");
                                        /*for (int j = 0; j<prodArray.length(); j++){
                                            JSONObject prodObj = prodArray.getJSONObject(i);
                                            Product product = new Product();
                                        }*/
                                        OrdersG ordersG = new OrdersG(ordObj.getString("oid"),
                                                ordObj.getString("orderts"), ordObj.getString("total"),
                                                ordObj.getJSONObject("customer_info").getString("cname"),
                                                ordObj.getJSONObject("merchant_info").getString("mshopname"),
                                                prodArray);
                                        myDataset.add(i, ordersG);
                                    }


                                } else if (status.equals("-1")) {
                                    Toast.makeText(getApplicationContext(), "Please Try again after sometime", Toast.LENGTH_SHORT).show();
                                } else if ("-3".equals(status)) {
                                    Toast.makeText(getApplicationContext(), "No Orders", Toast.LENGTH_SHORT).show();
                                }
                                mAdapter.notifyDataSetChanged();
                                //mCardRequested = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put(EndPoints.MERCHANT_ID, MID);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    // params.put("Content-Type","application/json");
                    return params;
                }

            };
            Log.e(TAG, "StrReq:" + strReq);
            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        }
    }
}
