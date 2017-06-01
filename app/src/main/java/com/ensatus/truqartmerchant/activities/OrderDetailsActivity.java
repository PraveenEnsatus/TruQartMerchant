package com.ensatus.truqartmerchant.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.adapter.OrderDetailAdapter;
import com.ensatus.truqartmerchant.module.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderDetailsActivity extends AppCompatActivity {
    private static final String TAG = OrderDetailsActivity.class.getSimpleName();
    private String stringJsonArray;
    private List<Product> myDataset = new ArrayList<>();
    private List<Product> myDataset1 = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvRetailerName;
    private TextView tvOrderId;
    private TextView tvOrderTS;
    private TextView tvGrandTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent m = getIntent();
        stringJsonArray = getIntent().getStringExtra("parray");

        tvGrandTotal = (TextView) findViewById(R.id.total);
        tvOrderId = (TextView) findViewById(R.id.o_id);
        tvOrderTS = (TextView) findViewById(R.id.order_on);
        tvRetailerName = (TextView) findViewById(R.id.r_name);
        tvRetailerName.setText(m.getStringExtra("cname"));
        tvOrderTS.setText(m.getStringExtra("orderts"));
        tvOrderId.setText(m.getStringExtra("oid"));
        tvGrandTotal.setText(m.getStringExtra("total"));

        mRecyclerView = (RecyclerView) findViewById(R.id.prod_rec);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myDataset1 = getMyDataset();
        // specify an adapter (see also next example)
        mAdapter = new OrderDetailAdapter(myDataset1, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        Log.e(TAG, "Adapter: " + myDataset);
        mAdapter.notifyDataSetChanged();

    }

    public List<Product> getMyDataset(){

        try {
            JSONArray jsonArray1 = new JSONArray(stringJsonArray);
            for(int j = 0; j<jsonArray1.length(); j++) {
                JSONObject pro = jsonArray1.getJSONObject(j);
                Product prod = new Product(pro.getString("name"),
                        pro.getString("price"),
                        pro.getString("quantity"));
                myDataset.add(prod);
                Log.e(TAG," p Array:"+myDataset);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return myDataset;
    }
}
