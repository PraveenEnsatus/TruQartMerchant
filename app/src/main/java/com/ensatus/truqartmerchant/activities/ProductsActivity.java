package com.ensatus.truqartmerchant.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.adapter.ProductAdapter;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.fragments.AddEventsFrag;
import com.ensatus.truqartmerchant.fragments.EventsFrag;
import com.ensatus.truqartmerchant.fragments.ProductsFrag;
import com.ensatus.truqartmerchant.module.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity {
    private  static String TAG = ProductsActivity.class.getSimpleName();
    protected boolean mCardRequested;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Product> myDataset = new ArrayList<>();
    private String mProName, mProPrice;

    private static String MID = "745d03d7-6e32-4db7-a991-5e5cf664e8e6";
    private static String CAT_ID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container_frame, new ProductsFrag()).commit();


       /* mCardRequested = false;
        mRecyclerView = (RecyclerView) findViewById(R.id.prod_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use Grid Layout Manager for grid view
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify  Adapter
        mAdapter = new ProductAdapter(myDataset,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        try {
            getProduct();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();

        mRecyclerView.addOnItemTouchListener(new MyApplication.RecyclerTouchListener(getApplicationContext(), mRecyclerView, new MyApplication.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Product product = myDataset.get(position);
                Toast.makeText(getApplicationContext(), product.getmProName() + " is selected!", Toast.LENGTH_SHORT).show();
                String name = product.getmProName();
                Log.e("name", "" + name);
                Intent intent = new Intent(ProductsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("Product_name", name);
                intent.putExtra("Product_cat", product.getmCatId());
                intent.putExtra("Product_price", product.getmPrice());
                intent.putExtra("Pro_Img", product.getmImage());
                intent.putExtra("Pro_desc", product.getmDescription());
                intent.putExtra("Pro_Id", product.getmPId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_pro_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               *//* FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddEventsFrag addEventsFrag = new AddEventsFrag();
                myDataset.clear();
                fragmentTransaction.replace(R.id.container_frame, addEventsFrag).addToBackStack(null).commit();*//*

            }
        });*/
    }


   /* public void getProduct() throws JSONException {
        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.PRODUCT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e(TAG, "response: "+response.toString());

                            JSONObject obj = new JSONObject(response);
                            System.out.print(obj);
                            String status = obj.getString(EndPoints.STATUS);
                            String message = obj.getString(EndPoints.MESSAGE);
                            if (message.equals("SUCCESS")&& status.equals("100")){
                                JSONArray product = obj.getJSONArray(EndPoints.ALL_PRODUCTS);
                                for (int i=0; i< product.length(); i++) {
                                    JSONObject c = product.getJSONObject(i);
                                    JSONObject p = c.getJSONObject(EndPoints.PRODUCT);
                                    Product prod = new Product(p.getString(EndPoints.PROD_NAME),
                                            p.getString(EndPoints.PROD_PRICE),
                                            p.getString(EndPoints.PROD_IMAGE1),
                                            c.getString(EndPoints.PROD_PID),
                                            p.getString(EndPoints.PROD_DESC), p.getString(EndPoints.PROD_LIKES),
                                            c.getString(EndPoints.CATEGORY_ID));
                                    myDataset.add(i, prod);
                                    Log.e(TAG, "product: " + prod.getmProName() + prod.getmImage()+"  "+prod.getmPId()+prod.getmCatId());
                                    mProName = prod.getmProName();
                                    mProPrice = prod.getmPrice();
                                }
                            }else if (status.equals("-1")){
                                Toast.makeText(getApplicationContext(),"Products Not Found", Toast.LENGTH_SHORT).show();
                            }
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
                params.put(EndPoints.MERCHANT_ID,MID);
                params.put(EndPoints.CATEGORY_ID, CAT_ID);

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
    }
*/





}
