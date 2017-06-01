package com.ensatus.truqartmerchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.module.Product;

import java.util.List;

/**
 * Created by Praveen on 22-05-2017.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<Product>  mDataset;
    private Context mContext;
    private static final String TAG = OrderDetailAdapter.class.getSimpleName();


    public OrderDetailAdapter(List<Product> mDataset, Context mContext) {
        this.mDataset = mDataset;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_prod_card, parent, false);
        //set the view's size, margins, paddings and layout parameters

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Product product = mDataset.get(position);
        holder.tvQty.setText(product.getmQty());
        holder.tvProdName.setText(product.getmProName());
        float x = Integer.valueOf(product.getmQty()) * Float.valueOf(product.getmPrice());
        holder.tvAmount.setText(mContext.getResources().getString(
                R.string.rupee)+String.valueOf(x));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProdName;
        TextView tvQty;
        TextView tvAmount;


        public ViewHolder(View itemView) {
            super(itemView);
            tvAmount = (TextView) itemView.findViewById(R.id.pro_amount);
            tvProdName = (TextView) itemView.findViewById(R.id.pro_name);
            tvQty = (TextView) itemView.findViewById(R.id.pro_qty);
        }
    }
}
