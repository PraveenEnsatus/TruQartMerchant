package com.ensatus.truqartmerchant.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.activities.ProductDetailsActivity;
import com.ensatus.truqartmerchant.activities.ProductsActivity;
import com.ensatus.truqartmerchant.adapter.ProductAdapter;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.module.Product;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Praveen on 20-03-2017.
 */

public class   ProductsFrag extends Fragment {
    private  static final String TAG = ProductsActivity.class.getSimpleName();
    protected boolean mCardRequested;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> myDataset = new ArrayList<>();
    private String mProName, mProPrice;

    private static String MID;
    private static String CAT_ID = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        User user = MyApplication.getInstance().getPrefManager().getUser();
        MID = user.getmId();
        final View mRootView = inflater.inflate(R.layout.products_frag, container, false);
        mCardRequested = false;
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.prod_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use Grid Layout Manager for grid view
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify  Adapter
        mAdapter = new ProductAdapter(myDataset,getContext());
        mRecyclerView.setAdapter(mAdapter);

        try {
            getProduct();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();

        mRecyclerView.addOnItemTouchListener(new MyApplication.RecyclerTouchListener(getContext(), mRecyclerView, new MyApplication.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Product product = myDataset.get(position);
                Toast.makeText(getContext(), product.getmProName() + " is selected!", Toast.LENGTH_SHORT).show();
                String name = product.getmProName();
                Log.e("name", "" + name);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("Product_name", name);
                intent.putExtra("Product_cat", product.getmCatId());
                intent.putExtra("Product_price", product.getmPrice());
                intent.putExtra("Pro_Img", product.getmImage());
                intent.putExtra("Pro_unit", product.getmUnit());
                intent.putExtra("Pro_Id", product.getmPId());
                intent.putExtra("Pro_Disc",product.getmDisc());

                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.add_pro_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddProductsFrag addProductsFrag = new AddProductsFrag();
                myDataset.clear();
                fragmentTransaction.replace(R.id.container_frame, addProductsFrag).addToBackStack(null).commit();

            }
        });

        return mRootView;
    }

    public void getProduct() throws JSONException {
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
                                            c.getString(EndPoints.CATEGORY_ID),p.getString(EndPoints.PROD_UNIT),
                                            p.getString("discount"));
                                    myDataset.add(i, prod);
                                    Log.e(TAG, "product: " + prod.getmProName() + prod.getmImage()+"  "+prod.getmPId()+prod.getmCatId());
                                    mProName = prod.getmProName();
                                    mProPrice = prod.getmPrice();
                                }
                            }else if (status.equals("-1")){
                                Toast.makeText(getContext(),"Products Not Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
}

