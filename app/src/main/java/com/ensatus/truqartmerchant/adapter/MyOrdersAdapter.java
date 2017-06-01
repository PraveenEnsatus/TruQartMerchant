package com.ensatus.truqartmerchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.module.OrdersG;

import java.util.ArrayList;

/**
 * Created by Praveen on 22-02-2017.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private static String TAG = MyOrdersAdapter.class.getSimpleName();
   /* private ArrayList<Order> mDataset;*/
   private ArrayList<OrdersG> mDataset;
    ImageLoader imageLoader;
    private Context mContext;

    public MyOrdersAdapter(ArrayList<OrdersG> myDataset, Context context){
        mDataset = myDataset;
        this.mContext = context;
    }

    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.groc_order_card, parent,false);
        // set the view's size, margins, paddings and layout parameters
        MyOrdersAdapter.ViewHolder vh = new MyOrdersAdapter.ViewHolder(itemView);
        return vh;

    }

    @Override
    public void onBindViewHolder(MyOrdersAdapter.ViewHolder holder, int position) {
        final OrdersG order = mDataset.get(position);
        //final Order order = mDataset.get(position);
        /*if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        imageLoader.get(order.getpImg(), ImageLoader.getImageListener(holder.imageView, R.drawable.ordersicon, android.R.drawable.ic_dialog_alert));
        Log.e(TAG,"IMAGEURL :"+order.getpImg());

        holder.imageView.setImageUrl(order.getpImg(), imageLoader);

        holder.proQty.setText("Qty "+order.getpQty()+" pcs");
        //holder.proDesc.setText(product.getmDescription());
        holder.proName.setText(order.getpName());
        holder.proPrice.setText(mContext.getResources().getString(
                R.string.rupee)+" "+order.getpPrice()+"/-");
        holder.proStatus.setText(order.getpStatus());
        holder.proTotal.setText(mContext.getResources().getString(
                R.string.rupee)+" "+String.valueOf(Integer.parseInt(order.getpQty())*(Integer.parseInt(order.getpPrice()))) );
        if (order.getpStatus().equals("Delivered")){

            holder.proBtnLyt.setVisibility(View.GONE);
            holder.proDetailsLyt.setVisibility(View.GONE);
        }*/
        holder.mRetailerName.setText(order.getCname());
        holder.mOrderON.setText(order.getOrderts());
        holder.mShopName.setText(order.getMshopname());
        holder.mTotal.setText(order.getTotal());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder{

         NetworkImageView imageView;
         TextView proName, proPrice, proQty, proTotal;
         Button proStatus;
         LinearLayout proDetailsLyt, proBtnLyt;

         TextView mRetailerName, mShopName, mOrderON, mTotal;

         ViewHolder(View itemView) {
            super(itemView);

            /*imageView = (NetworkImageView) itemView.findViewById(R.id.p_img);
            proName = (TextView) itemView.findViewById(R.id.p_name);
            proPrice = (TextView) itemView.findViewById(R.id.p_price);
            proQty = (TextView) itemView.findViewById(R.id.p_qty);
            proStatus = (Button) itemView.findViewById(R.id.p_status);
             proTotal = (TextView) itemView.findViewById(R.id.p_total);
             proDetailsLyt = (LinearLayout) itemView.findViewById(R.id.details_lyt);
             proBtnLyt = (LinearLayout) itemView.findViewById(R.id.btn_lyt);*/
            mRetailerName = (TextView) itemView.findViewById(R.id.get_retail_name);
            mOrderON = (TextView) itemView.findViewById(R.id.get_order_on);
            mShopName = (TextView) itemView.findViewById(R.id.get_shop_name);
             mTotal = (TextView) itemView.findViewById(R.id.get_total);
        }

    }
}
